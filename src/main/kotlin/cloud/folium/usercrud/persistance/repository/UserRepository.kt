package cloud.folium.usercrud.persistance.repository

import cloud.folium.usercrud.business.entity.User
import kotlin.Throws
import java.sql.SQLException
import java.util.*

interface UserRepository {
    @Throws(SQLException::class)
    fun findByEmail(email: String?): User?

    @Throws(SQLException::class)
    fun getByEmail(email: String?): User?

    @Throws(SQLException::class)
    fun existsByEmail(email: String?): Boolean

    @Throws(SQLException::class)
    fun deleteByEmail(email: String?)

    @Throws(SQLException::class)
    fun findAllByCountry(country: String?): List<User>

    @Throws(SQLException::class)
    fun findAll(): List<User>
    fun save(user: User?): User?

    @Throws(SQLException::class)
    fun findById(id: Long): User?

    @Throws(SQLException::class)
    fun deleteById(id: Long)

    @Throws(SQLException::class)
    fun existsById(id: Long): Boolean

}