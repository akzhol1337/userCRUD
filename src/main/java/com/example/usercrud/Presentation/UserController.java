package com.example.usercrud.Presentation;

import com.example.usercrud.Business.Entity.ErrorResponse;
import com.example.usercrud.Business.Entity.User;
import com.example.usercrud.Business.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    final UserService userService;
    ErrorResponse errorResponse;

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @GetMapping("/lol")
    public void kek(HttpServletRequest request) throws JsonProcessingException {
        String userIP = "84.240.214.26";
        System.out.println(userIP);


        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject("http://ip-api.com/json/" + userIP + "?fields=status,country", String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(result, Map.class);
        System.out.println((String) map.get("country"));

    }

    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody User user, HttpServletRequest request) throws JsonProcessingException {
        errorResponse = new ErrorResponse(new Date());
        if(user == null){
            errorResponse.getErrorDescriptions().add("No user info");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if(user.getFirstName() == null || user.getFirstName().isEmpty()){
            errorResponse.getErrorDescriptions().add("First name is empty");
        }

        if(user.getEmail() == null){
            errorResponse.getErrorDescriptions().add("No email");
        } else if (!isValidEmailAddress(user.getEmail())){
            errorResponse.getErrorDescriptions().add("Invalid email");
        }

        if(userService.existsByEmail(user.getEmail())){
            errorResponse.getErrorDescriptions().add("User with this email already exists");
        }
        if(user.getGender() == null){
            errorResponse.getErrorDescriptions().add("No gender");
        }
        if(!errorResponse.getErrorDescriptions().isEmpty()){
            return ResponseEntity.badRequest().body(errorResponse);
        }
        String userIP = request.getRemoteAddr();
        System.out.println(userIP);

        // if we run app on localhost getRemoteAddr() always returns smth like localhost;127.0.0.1;0:0:0:0:0:0:0:1
        if(Objects.equals(userIP, "0:0:0:0:0:0:0:1") || Objects.equals(userIP, "127.0.0.1")){
            System.out.println(userIP);
            user.setCountry("Kazakhstan");
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject("http://ip-api.com/json/" + userIP + "?fields=status,country", String.class);

            ObjectMapper mapper = new ObjectMapper();
            Map map = mapper.readValue(result, Map.class);

            if(map.get("status") == "fail"){
                user.setCountry("undefined");
            } else {
                user.setCountry((String) map.get("country"));
            }
        }
        return ResponseEntity.ok(userService.addUser(user));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity deleteUserById(@PathVariable Long id){
        errorResponse = new ErrorResponse(new Date());
        Optional<User> user = userService.deleteById(id);
        if(user.isEmpty()){
            errorResponse.getErrorDescriptions().add("User with this id doesn't exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(user.get());
    }

    @DeleteMapping("/email/{email}")
    public ResponseEntity deleteUserByEmail(@PathVariable String email){
        errorResponse = new ErrorResponse(new Date());
        Optional<User> user = userService.deleteByEmail(email);
        if(user.isEmpty()){
            errorResponse.getErrorDescriptions().add("User with this email doesn't exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/id/{id}")
    public ResponseEntity updateUserById(@PathVariable Long id, @RequestBody User newUser){
        errorResponse = new ErrorResponse(new Date());
        Optional<User> user = userService.updateById(id, newUser);
        if(user.isEmpty()){
            errorResponse.getErrorDescriptions().add("User with this id doesn't exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/email/{email}")
    public ResponseEntity updateUserByEmail(@PathVariable String email, @RequestBody User newUser){
        errorResponse = new ErrorResponse(new Date());
        Optional<User> user = userService.updateByEmail(email, newUser);
        if(user.isEmpty()){
            errorResponse.getErrorDescriptions().add("User with this email doesn't exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(user.get());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity getUserById(@PathVariable Long id){
        errorResponse = new ErrorResponse(new Date());
        Optional<User> user = userService.findById(id);
        if(user.isEmpty()){
            errorResponse.getErrorDescriptions().add("User with this id doesn't exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(user);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email){
        errorResponse = new ErrorResponse(new Date());
        Optional<User> user = userService.findByEmail(email);
        if(user.isEmpty()){
            errorResponse.getErrorDescriptions().add("User with this email doesn't exist");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        return ResponseEntity.ok(user);
    }
}
