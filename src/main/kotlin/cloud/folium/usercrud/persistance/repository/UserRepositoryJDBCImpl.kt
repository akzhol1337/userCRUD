package cloud.folium.usercrud.persistance.repository

import cloud.folium.usercrud.business.entity.User
import kotlin.Throws
import org.springframework.stereotype.Repository
import java.lang.Exception
import java.sql.Connection
import java.sql.SQLException
import java.util.*

class UserRepositoryJDBCImpl : UserRepository {
    private val connection: Connection? = null
    @Throws(SQLException::class)
    override fun findByEmail(email: String?): User? {
        var user: User? = null
        try {
            connection!!.prepareStatement("SELECT * FROM users WHERE email=?").use { statement ->
                statement.setString(1, email)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    user =
                        User(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8)
                        )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return user
    }

    @Throws(SQLException::class)
    override fun getByEmail(email: String?): User? {
        var user: User? = null
        try {
            connection!!.prepareStatement("SELECT * FROM users WHERE email=?").use { statement ->
                statement.setString(1, email)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    user = User(
                        resultSet.getLong(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getInt(6),
                        resultSet.getString(7),
                        resultSet.getString(8)
                    )
                }
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return user
    }

    @Throws(SQLException::class)
    override fun existsByEmail(email: String?): Boolean {
        var exists = false
        try {
            connection!!.prepareStatement("SELECT * FROM users WHERE email=?").use { statement ->
                statement.setString(1, email)
                exists = statement.execute()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return exists
    }

    @Throws(SQLException::class)
    override fun deleteByEmail(email: String?) {
        try {
            connection!!.prepareStatement("DELETE * FROM users WHERE email=?").use { statement ->
                statement.setString(1, email)
                statement.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
    }

    @Throws(SQLException::class)
    override fun findAllByCountry(country: String?): List<User> {
        val users = ArrayList<User>()
        try {
            connection!!.prepareStatement("SELECT * FROM users WHERE country=?").use { statement ->
                statement.setString(1, country)
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    users.add(
                        User(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return users
    }

    @Throws(SQLException::class)
    override fun findAll(): List<User> {
        val users = ArrayList<User>()
        try {
            connection!!.createStatement().use { statement ->
                val resultSet = statement.executeQuery("SELECT * FROM users")
                while (resultSet.next()) {
                    users.add(
                        User(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return users
    }

    override fun save(user: User?): User? {
        var savedUser: User? = null
        try {
            connection!!.prepareStatement("INSERT INTO users (first_name, last_name, middle_name, country, gender, email, avatar) VALUES(?, ?, ?, ?, ?, ?, ?)")
                .use { statement ->
                    statement.setString(1, user?.firstName)
                    statement.setString(2, user?.lastName)
                    statement.setString(3, user?.middleName)
                    statement.setString(4, user?.country)
                    // todo
                    user?.gender?.let { statement.setInt(5, it) }
                    statement.setString(6, user?.email)
                    statement.setString(7, user?.avatar)
                    statement.executeUpdate()
                    savedUser = getByEmail(user?.email)
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return savedUser
    }

    @Throws(SQLException::class)
    override fun findById(id: Long): User? {
        var user: User? = null
        try {
            connection!!.prepareStatement("SELECT * FROM users WHERE id=?").use { statement ->
                statement.setLong(1, id)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    user =
                        User(
                            resultSet.getLong(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5),
                            resultSet.getInt(6),
                            resultSet.getString(7),
                            resultSet.getString(8)
                        )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
        return user
    }

    @Throws(SQLException::class)
    override fun deleteById(id: Long) {
        try {
            connection!!.prepareStatement("DELETE * FROM users WHERE id=?").use { statement ->
                statement.setLong(1, id)
                statement.executeUpdate()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
    }

    @Throws(SQLException::class)
    override fun existsById(id: Long): Boolean {
        var exists = try {
            connection!!.prepareStatement("SELECT * FROM users WHERE id=?").use { statement ->
                statement.setLong(1, id)
                statement.execute()
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            throw e
        }
        return exists
    }
}