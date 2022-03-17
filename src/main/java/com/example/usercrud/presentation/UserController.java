package com.example.usercrud.presentation;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.business.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    final private UserService userService;

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        System.out.println(user.toString());
        if(bindingResult.hasErrors()){
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errorMessages.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Optional<User> newUser = userService.addUser(user, request);
        if(newUser.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + user.getEmail() + " already exist"));
        }
        log.info("Successfully added user with id {}", newUser.get().getId());
        return ResponseEntity.ok(newUser.get());
    }

    @DeleteMapping("/user/id/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id){
        if(userService.deleteById(id)){
            log.info("Successfully deleted user with id: {}", id);
            return (ResponseEntity) ResponseEntity.ok(Collections.singletonMap("success", "Deleted user with id " + id));
        }

        System.out.println("Akzhol");
        log.warn("Tried to delete nonexistent user with id: {}", id);
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with id " + id + " doesn't exist"));
    }

    @GetMapping("/users/{country}")
    public ResponseEntity getAllUsersByCountry(@PathVariable String country){
        log.info("Getting all users from: {} country", country);
        return ResponseEntity.ok(userService.getAllByCountry(country));
    }

    @GetMapping("/users/{country}/{pageNumber}/{pageSize}")
    public ResponseEntity getAllUsersByCountry(@PathVariable String country, @PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        log.info("Getting all users from: {} country, page number: {}, page size: {}", country, pageNumber, pageSize);
        return ResponseEntity.ok(userService.getPageByCountry(country, pageNumber, pageSize));
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        log.info("Getting all users");
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{pageNumber}/{pageSize}")
    public ResponseEntity getAllUsers(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        log.info("Getting all users, page number: {}, page size: {}", pageNumber, pageSize);
        return ResponseEntity.ok(userService.getPage(pageNumber, pageSize));
    }

    @DeleteMapping("/user/email/{email}")
    public ResponseEntity deleteUserByEmail(@PathVariable String email){
        Boolean deleted = userService.deleteByEmail(email);
        if(deleted){
            log.info("Successfully deleted user with email: {}", email);
            return ResponseEntity.ok(Collections.singletonMap("success", "Deleted user with email " + email));
        }
        log.warn("Tried to delete nonexistent user with email: {}", email);
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + email + " doesn't exist"));
    }

    @PutMapping("/user/id/{id}")
    public ResponseEntity updateUserById(@PathVariable Long id, @RequestBody User newUser){
        Optional<User> user = userService.updateById(id, newUser);
        if(user.isEmpty()){
            log.warn("Tried to update nonexistent user with id: {}", id);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with id " + id + " doesn't exist"));
        }
        log.info("Successfully updated user with id: {}", id);
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/user/email/{email}")
    public ResponseEntity updateUserByEmail(@PathVariable String email, @RequestBody User newUser){
        Optional<User> user = userService.updateByEmail(email, newUser);
        System.out.println(user.isEmpty());
        if(user.isEmpty()){
            log.warn("Tried to update nonexistent user with email: {}", email);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + email + " doesn't exist"));
        }
        log.info("Successfully updated user with email: {}", email);
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity getUserById(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        if(user.isEmpty()){
            log.warn("Tried to get nonexistent user with id: {}", id);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with id " + id + " doesn't exist"));
        }
        log.info("Successfully get user with id: {}", id);
        return ResponseEntity.ok(user.get());
    }
    @GetMapping("/user/email/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email){
        Optional<User> user = userService.findByEmail(email);
        if(user.isEmpty()){
            log.warn("Tried to get nonexistent user with email: {}", email);
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + email + " doesn't exist"));
        }
        log.info("Successfully get user with email: {}", email);
        return ResponseEntity.ok(user.get());
    }
}
