package com.example.usercrud.persistance.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Test
    public void contextLoads(){
        assertThat(userRepo).isNotNull();
    }
}