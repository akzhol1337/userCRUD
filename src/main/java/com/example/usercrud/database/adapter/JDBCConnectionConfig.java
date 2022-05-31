package com.example.usercrud.database.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@Slf4j
public class JDBCConnectionConfig {
    @Bean
    public Connection getConnection(@Value("${jdbc.driverName}") String jdbcDriverName, @Value("${jdbc.connectionURL}")String jdbcConnectionURL) throws SQLException, ClassNotFoundException {
        try {
            Class.forName(jdbcDriverName);
            return DriverManager.getConnection(jdbcConnectionURL, "admin", "password");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
