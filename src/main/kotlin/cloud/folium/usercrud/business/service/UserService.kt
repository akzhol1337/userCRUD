package cloud.folium.usercrud.business.service

import cloud.folium.usercrud.business.entity.User
import javax.servlet.http.HttpServletRequest
import cloud.folium.usercrud.business.entity.UserRequestDto
import cloud.folium.usercrud.persistance.repository.UserRepository

interface UserService {
    fun addUser(user: User?): User?
    fun addUser(user: User?, request: HttpServletRequest?): User?
    fun addUser(userRequestDto: UserRequestDto?, request: HttpServletRequest?): User?
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun deleteById(id: Long): Boolean
    fun deleteByEmail(email: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun updateByEmail(email: String, newUser: User): User?
    fun getAllByCountry(country: String): List<User>
    fun getAll(): List<User>
    fun updateById(id: Long, newUser: User): User?
    fun setRepository(userRepository: UserRepository)
}