package com.example.usercrud.persistance.repository;

import com.example.usercrud.business.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    List<User> findAllByCountry(String country, Pageable pageable);
    List<User> findAllByCountry(String country);
}
