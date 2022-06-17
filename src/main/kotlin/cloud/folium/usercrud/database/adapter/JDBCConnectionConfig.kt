package cloud.folium.usercrud.database.adapter

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import kotlin.Throws

@Configuration
class JDBCConnectionConfig {
    @Bean
    @Throws(SQLException::class, ClassNotFoundException::class)
    fun getConnection(
        /*@Value("\${jdbc.driverName}") jdbcDriverName: String?,
        @Value("\${jdbc.connectionURL}") jdbcConnectionURL: String?

         */
    ): Connection {
        return try {
            val jdbcDriverName: String = "org.h2.Driver"
            val jdbcConnectionURL: String = "jdbc:h2:file:./src/main/java/com/example/usercrud/Database/Database"
            Class.forName(jdbcDriverName)
            DriverManager.getConnection(jdbcConnectionURL, "admin", "password")
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}