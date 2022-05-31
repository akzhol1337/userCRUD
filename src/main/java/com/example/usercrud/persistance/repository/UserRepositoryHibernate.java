package com.example.usercrud.persistance.repository;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.business.entity.annotations.Loggable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Loggable
public interface UserRepositoryHibernate extends JpaRepository<User, Long>, UserRepository {
}
