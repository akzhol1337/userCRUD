package com.example.usercrud.business.service;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.business.entity.UserRequestDto;
import com.example.usercrud.persistance.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface UserService {
    Optional<User> addUser(User user);

    Optional<User> addUser(User user, HttpServletRequest request) throws JsonProcessingException;

    Optional<User> addUser(UserRequestDto userRequestDto, HttpServletRequest request) throws IOException;

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Boolean deleteById(Long id);

    Boolean deleteByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> updateByEmail(String email, User newUser);

    //List<User> getPageByCountry(String country, Integer pageNumber, Integer pageSize);

    List<User> getAllByCountry(String country);

    //Page<User> getPage(Integer pageNumber, Integer pageSize);

    List<User> getAll();

    Optional<User> updateById(Long id, User newUser);

    //void setRepository(UserRepository userRepository);
}
