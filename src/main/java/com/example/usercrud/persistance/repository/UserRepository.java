package com.example.usercrud.persistance.repository;

import com.example.usercrud.business.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email) throws SQLException;
    User getByEmail(String email) throws SQLException;
    boolean existsByEmail(String email) throws SQLException;
    void deleteByEmail(String email) throws SQLException;
    List<User> findAllByCountry(String country) throws SQLException;
    List<User> findAll() throws SQLException;
    User save(User user);
    Optional<User> findById(Long id) throws SQLException;
    void deleteById(Long id) throws SQLException;
    boolean existsById(Long id) throws SQLException;
}
