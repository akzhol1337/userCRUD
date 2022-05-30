package com.example.usercrud.database.adapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class H2Adapter {
    @Bean
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String connectionURL = "jdbc:h2:file:./src/main/java/com/example/usercrud/Database/Database";
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(connectionURL, "admin", "password");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
