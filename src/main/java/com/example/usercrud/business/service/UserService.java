package com.example.usercrud.business.service;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.persistance.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    final UserRepository userRepo;
    final RestTemplate restTemplate;

    public Optional<User> addUser(User user){
        Optional<User> newUser = Optional.empty();
        if(!userRepo.existsByEmail(user.getEmail())){
            newUser = Optional.of(userRepo.save(user));
        }
        return newUser;
    }

    public Optional<User> addUser(User user, HttpServletRequest request) throws JsonProcessingException {
        String userIP = request.getRemoteAddr();
        if(Objects.equals(userIP, "0:0:0:0:0:0:0:1") || Objects.equals(userIP, "127.0.0.1")){
            user.setCountry("Kazakhstan");
        } else {
            String result = restTemplate.getForObject("http://ip-api.com/json/" + userIP + "?fields=status,country", String.class);
            System.out.println(userIP);
            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(result, Map.class);

            if(map.get("status").equals("fail")){
                // if it's private ip
                user.setCountry("Kazakhstan");
            } else {
                user.setCountry((String) map.get("country"));
            }
        }
        Optional<User> newUser = Optional.empty();
        if(!userRepo.existsByEmail(user.getEmail())){
            newUser = Optional.of(userRepo.save(user));
        }
        return newUser;
    }

    public Optional<User> findById(Long id){
        return userRepo.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public Boolean deleteById(Long id){
        if(userRepo.existsById(id)){
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Boolean deleteByEmail(String email){
        if(userRepo.existsByEmail(email)){
            userRepo.deleteByEmail(email);
            return true;
        }
        return false;
    }

    public boolean existsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public Optional<User> updateByEmail(String email, User newUser){
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()){
            user.get().setEmail(newUser.getEmail());
            user.get().setCountry(newUser.getCountry());
            user.get().setFirstName(newUser.getFirstName());
            user.get().setLastName(newUser.getLastName());
            user.get().setMiddleName(newUser.getMiddleName());
            user.get().setGender(newUser.getGender());
        }
        return user;
    }

    public List<User> getPageByCountry(String country, Integer pageNumber, Integer pageSize){
        return userRepo.findAllByCountry(country, PageRequest.of(pageNumber, pageSize));
    }

    public List<User> getAllByCountry(String country){
        return userRepo.findAllByCountry(country);
    }

    public Page<User> getPage(Integer pageNumber, Integer pageSize){
        return userRepo.findAll(PageRequest.of(pageNumber, pageSize));
    }

    public List<User> getAll(){
        return userRepo.findAll();
    }

    public Optional<User> updateById(Long id, User newUser){
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            user.get().setEmail(newUser.getEmail());
            user.get().setCountry(newUser.getCountry());
            user.get().setFirstName(newUser.getFirstName());
            user.get().setLastName(newUser.getLastName());
            user.get().setMiddleName(newUser.getMiddleName());
            user.get().setGender(newUser.getGender());
        }
        return user;
    }
}
