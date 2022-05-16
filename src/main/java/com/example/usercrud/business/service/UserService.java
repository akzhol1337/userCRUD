package com.example.usercrud.business.service;

import com.example.usercrud.business.entity.annotations.Metric;
import com.example.usercrud.business.entity.User;
import com.example.usercrud.persistance.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public interface UserService {
    public Optional<User> addUser(User user);
    public Optional<User> addUser(User user, HttpServletRequest request) throws JsonProcessingException;

    @Metric(name="retrieveById")
    public Optional<User> findById(Long id);

    public Optional<User> findByEmail(String email);

    public Boolean deleteById(Long id);

    public Boolean deleteByEmail(String email);

    public boolean existsByEmail(String email);

    public Optional<User> updateByEmail(String email, User newUser);

    public List<User> getPageByCountry(String country, Integer pageNumber, Integer pageSize);

    public List<User> getAllByCountry(String country);

    public Page<User> getPage(Integer pageNumber, Integer pageSize);

    public List<User> getAll();

    public Optional<User> updateById(Long id, User newUser);

    void setRepository(UserRepository userRepository);
}
