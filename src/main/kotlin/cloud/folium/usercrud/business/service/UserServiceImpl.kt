package cloud.folium.usercrud.business.service

import org.springframework.beans.factory.annotation.Autowired
import cloud.folium.usercrud.persistance.repository.UserRepository
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest
import cloud.folium.usercrud.business.entity.User
import kotlin.Throws
import com.amazonaws.services.s3.model.ObjectMetadata
import org.springframework.web.multipart.MultipartFile
import cloud.folium.usercrud.business.entity.UserRequestDto
import cloud.folium.usercrud.business.entity.annotations.Loggable
import cloud.folium.usercrud.business.entity.annotations.Metric
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.sql.SQLException
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class UserServiceImpl : UserService {
    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var s3Client: AmazonS3

    override fun addUser(user: User?): User? {
        var newUser:User? = null
        try {
            if (!userRepo.existsByEmail(user!!.email)) {
                newUser = userRepo.save(user)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return newUser
    }

    fun getCountryByHttpServletRequest(request: HttpServletRequest?): String {
        val userIP = request?.remoteAddr
        return if (userIP == "0:0:0:0:0:0:0:1" || userIP == "127.0.0.1") {
            "Kazakhstan"
        } else {
            val result = restTemplate.getForObject(
                "http://ip-api.com/json/$userIP?fields=status,country",
                String::class.java
            )
            val mapper = ObjectMapper()
            try {
                val map: Map<*, *> = mapper.readValue(result, MutableMap::class.java)
                if (map["status"] == "fail") {
                    "Kazakhstan"
                } else map["country"] as String
            } catch (e: JsonProcessingException) {
                "undefined"
            }
        }
    }

    fun getUserAvatarLinkAWSS3(decodedBase64: String, userEmail: String?): String {
        val encodedAvatar = Base64.decodeBase64(decodedBase64.substring(decodedBase64.indexOf(',') + 1).toByteArray())
        val inputStream: InputStream = ByteArrayInputStream(encodedAvatar)
        val metadata = ObjectMetadata()
        metadata.contentType = "image/png"
        metadata.contentLength = encodedAvatar.size.toLong()
        s3Client.putObject("usercrud-avatars", "$userEmail.png", inputStream, metadata)
        s3Client.setObjectAcl("usercrud-avatars", "$userEmail.png", CannedAccessControlList.PublicRead)
        return "https://storage.yandexcloud.net/usercrud-avatars/$userEmail.png"
    }

    @Throws(IOException::class)
    fun getUserAvatarLinkAWSS3(multipartFile: MultipartFile, userEmail: String?): String {
        val metadata = ObjectMetadata()
        val contentType = multipartFile.contentType
        metadata.contentType = contentType
        metadata.contentLength = multipartFile.bytes.size.toLong()
        val extension = '.'.toString() + contentType!!.substring(contentType.indexOf('/') + 1)
        s3Client.putObject("usercrud-avatars", userEmail + extension, multipartFile.inputStream, metadata)
        s3Client.setObjectAcl("usercrud-avatars", userEmail + extension, CannedAccessControlList.PublicRead)
        return "https://storage.yandexcloud.net/usercrud-avatars/$userEmail$extension"
    }

    override fun addUser(user: User?, request: HttpServletRequest?): User? {
        try {
            if (userRepo.existsByEmail(user!!.email)) {
                return null
            }
        } catch (e: Exception) {
            return null
        }
        user.country = getCountryByHttpServletRequest(request)
        if (user.avatar != null) {
            user.avatar = getUserAvatarLinkAWSS3(user.avatar!!, user.email)
        }
        return userRepo.save(user)
    }

    override fun addUser(userRequestDto: UserRequestDto?, request: HttpServletRequest?): User? {
        try {
            if (userRepo.existsByEmail(userRequestDto?.email)) {
                return null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
        val user = User(userRequestDto?.id, userRequestDto?.firstName, userRequestDto?.lastName, userRequestDto?.middleName, userRequestDto?.country, userRequestDto?.gender, userRequestDto?.email)
        user.country = getCountryByHttpServletRequest(request)
        if (userRequestDto?.avatar != null) {
            try {
                user.avatar = getUserAvatarLinkAWSS3(userRequestDto.avatar!!, userRequestDto.email)
            } catch (e: IOException) {
                user.avatar =
                    "https://static.wikia.nocookie.net/fnaf-fanon-animatronics/images/4/40/Банан.png/revision/latest?cb=20190614113143&path-prefix=ru"
            }
        }
        return userRepo.save(user)
    }

    @Metric(name = "retrieveById")
    override fun findById(id: Long): User? {
        return try {
            userRepo.findById(id)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    override fun findByEmail(email: String): User? {
        return try {
            userRepo.findByEmail(email)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }

    override fun deleteById(id: Long): Boolean {
        try {
            if (userRepo.existsById(id)) {
                userRepo.deleteById(id)
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    override fun deleteByEmail(email: String): Boolean {
        try {
            if (userRepo.existsByEmail(email)) {
                userRepo.deleteByEmail(email)
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    override fun existsByEmail(email: String): Boolean {
        try {
            return userRepo.existsByEmail(email)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    override fun updateByEmail(email: String, newUser: User): User? {
        val user: User = try {
            userRepo.findByEmail(email)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        } ?: return null
        user.email = newUser.email
        user.country = newUser.country
        user.firstName = newUser.firstName
        user.lastName = newUser.lastName
        user.middleName = newUser.middleName
        user.gender = newUser.gender
        if (newUser.avatar != null) {
            if (user.avatar != null) {
                s3Client.deleteObject("usercrud-avatars", user.email)
            }
            user.avatar = getUserAvatarLinkAWSS3(newUser.avatar!!, newUser.email)
        }
        return user
    }

    override fun getAllByCountry(country: String): List<User> {
        return try {
            userRepo.findAllByCountry(country)
        } catch (e: SQLException) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun getAll(): List<User> {
        return try {
            userRepo.findAll()
        } catch (e: SQLException) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun updateById(id: Long, newUser: User): User? {
        val user: User = try {
            userRepo.findById(id)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        } ?: return null
        user.email = newUser.email
        user.country = newUser.country
        user.firstName = newUser.firstName
        user.lastName = newUser.lastName
        user.middleName = newUser.middleName
        user.gender = newUser.gender
        if (newUser.avatar != null) {
            if (user.avatar != null) {
                s3Client.deleteObject("usercrud-avatars", user.email)
            }
            user.avatar = getUserAvatarLinkAWSS3(newUser.avatar!!, newUser.email)
        }
        return user
    }

    override fun setRepository(userRepository: UserRepository) {
        this.userRepo = userRepository
    }
}