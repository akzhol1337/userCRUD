package com.example.usercrud.persistance.repository;

import com.example.usercrud.business.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User getByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    List<User> findAllByCountry(String country);
    List<User> findAll();
    User save(User user);
    Optional<User> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
