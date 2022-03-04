package com.example.usercrud.Business.Service;

import com.example.usercrud.Business.Entity.User;
import com.example.usercrud.Persistance.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    final UserRepository userRepo;

    public User addUser(User user){
        return userRepo.save(user);
    }

    public Optional<User> findById(Long id){
        return userRepo.findById(id);
    }

    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }

    public Optional<User> deleteById(Long id){
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()) {
            userRepo.deleteById(id);
        }
        return user;
    }

    public Optional<User> deleteByEmail(String email){
        Optional<User> user = userRepo.findByEmail(email);
        user.ifPresent(value -> userRepo.deleteById(value.getId()));
        return user;
    }

    public boolean existsByEmail(String email){
        return userRepo.existsByEmail(email);
    }

    public Optional<User> updateByEmail(String email, User newUser){
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()){
            if(newUser.getGender() != null && !Objects.equals(newUser.getGender(), user.get().getGender())){
                user.get().setGender(newUser.getGender());
            }
            if(!Objects.equals(newUser.getFirstName(), user.get().getFirstName())){
                user.get().setFirstName(newUser.getFirstName());
            }
            if(newUser.getLastName() != null && !Objects.equals(newUser.getLastName(), user.get().getLastName())){
                user.get().setLastName(newUser.getLastName());
            }
            if(newUser.getMiddleName() != null && !Objects.equals(newUser.getMiddleName(), user.get().getMiddleName())){
                user.get().setMiddleName(newUser.getMiddleName());
            }
            if(!Objects.equals(newUser.getEmail(), user.get().getEmail())){
                user.get().setEmail(newUser.getEmail());
            }
        }
        return user;
    }

    public Optional<User> updateById(Long id, User newUser){
        Optional<User> user = userRepo.findById(id);
        if(user.isPresent()){
            if(newUser.getGender() != null && !Objects.equals(newUser.getGender(), user.get().getGender())){
                user.get().setGender(newUser.getGender());
            }
            if(newUser.getLastName() != null && !Objects.equals(newUser.getFirstName(), user.get().getFirstName())){
                user.get().setFirstName(newUser.getFirstName());
            }
            if(newUser.getLastName() != null && !Objects.equals(newUser.getLastName(), user.get().getLastName())){
                user.get().setLastName(newUser.getLastName());
            }
            if(newUser.getMiddleName() != null && !Objects.equals(newUser.getMiddleName(), user.get().getMiddleName())){
                user.get().setMiddleName(newUser.getMiddleName());
            }
            if(newUser.getEmail() != null && !Objects.equals(newUser.getEmail(), user.get().getEmail())){
                user.get().setEmail(newUser.getEmail());
            }
        }
        return user;
    }
}
