package com.example.usercrud.presentation;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.business.entity.annotations.Loggable;
import com.example.usercrud.business.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@Loggable
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user, BindingResult bindingResult, HttpServletRequest request) throws Exception {
        if(bindingResult.hasErrors()){
            Map<String, String> errorMessages = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err -> errorMessages.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errorMessages);
        }
        Optional<User> newUser = userService.addUser(user, request);
        if(newUser.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + user.getEmail() + " already exist"));
        }
        return ResponseEntity.ok(newUser.get());
    }

    @DeleteMapping("/user/id/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id){
        if(userService.deleteById(id)){
            return ResponseEntity.ok(Collections.singletonMap("success", "Deleted user with id " + id));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with id " + id + " doesn't exist"));
    }

    @GetMapping("/users/{country}")
    public ResponseEntity getAllUsersByCountry(@PathVariable String country){
        return ResponseEntity.ok(userService.getAllByCountry(country));
    }

    @GetMapping("/users/{country}/{pageNumber}/{pageSize}")
    public ResponseEntity getAllUsersByCountry(@PathVariable String country, @PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        return ResponseEntity.ok(userService.getPageByCountry(country, pageNumber, pageSize));
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/users/{pageNumber}/{pageSize}")
    public ResponseEntity getAllUsers(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        return ResponseEntity.ok(userService.getPage(pageNumber, pageSize));
    }

    @DeleteMapping("/user/email/{email}")
    public ResponseEntity deleteUserByEmail(@PathVariable String email){
        Boolean deleted = userService.deleteByEmail(email);
        if(deleted){
            return ResponseEntity.ok(Collections.singletonMap("success", "Deleted user with email " + email));
        }
        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + email + " doesn't exist"));
    }

    @PutMapping("/user/id/{id}")
    public ResponseEntity updateUserById(@PathVariable Long id, @RequestBody User newUser){
        Optional<User> user = userService.updateById(id, newUser);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with id " + id + " doesn't exist"));
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/user/email/{email}")
    public ResponseEntity updateUserByEmail(@PathVariable String email, @RequestBody User newUser){
        Optional<User> user = userService.updateByEmail(email, newUser);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + email + " doesn't exist"));
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/user/id/{id}")
    public ResponseEntity getUserById(@PathVariable Long id){
        Optional<User> user = userService.findById(id);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with id " + id + " doesn't exist"));
        }
        return ResponseEntity.ok(user.get());
    }
    @GetMapping("/user/email/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email){
        Optional<User> user = userService.findByEmail(email);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "User with email " + email + " doesn't exist"));
        }
        return ResponseEntity.ok(user.get());
    }
}
