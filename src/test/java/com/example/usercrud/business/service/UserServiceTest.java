package com.example.usercrud.business.service;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.persistance.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserRepository userRepoTest;

    @Autowired
    private UserService userServiceTest;


    @Test
    @DisplayName("Delete not existing user by id")
    void itShouldDeleteNotExistingUserById() {
        //given
        Long id = -1L;

        //when
        Boolean expectedFalse = userServiceTest.deleteById(id);

        //then
        assertThat(expectedFalse).isFalse();
    }
    @Test
    @DisplayName("Delete existing user by id")
    void itShouldDeleteExistingUserById(){
        //given
        User user = new User(1L, "firstname", "secondname", "middlename", "country", 1, "email@email.email");
        userRepoTest.save(user);

        //when
        Boolean exceptedTrue = userServiceTest.deleteById(1L);

        //then
        assertThat(exceptedTrue).isTrue();
    }

    @Test
    @DisplayName("Delete not existing user by email")
    void itShouldDeleteNotExistingUserByEmail() {
        //given
        String notExistingEmail = "notexisting@email.email";

        //when
        Boolean expectedFalse = userServiceTest.deleteByEmail(notExistingEmail);

        //then
        assertThat(expectedFalse).isFalse();
    }


    @Test
    @DisplayName("Delete existing user by email")
    void itShouldDeleteExistingUserByEmail() {
        //given
        User user = new User(1L, "firstname", "secondname", "middlename", "country", 1, "email@email.email");
        userRepoTest.save(user);

        //when
        Boolean exceptedTrue = userServiceTest.deleteByEmail("email@email.email");

        //then
        assertThat(exceptedTrue).isTrue();
    }

    @Test
    @DisplayName("Check if not existing user exists by email")
    void itShouldCheckIfNotExistingUserExistsByEmail() {
        //given
        String notExistingEmail = "notexisting@email.email";

        //when
        boolean expectedFalse = userServiceTest.existsByEmail(notExistingEmail);

        //then
        assertThat(expectedFalse).isFalse();
    }

    @Test
    @DisplayName("Check if existing user exists by email")
    void itShouldCheckIfExistingUserExistsByEmail() {
        //given
        String existingEmail = "email@email.email";
        User user = new User(1L, "firstname", "secondname", "middlename", "country", 1, existingEmail);
        userServiceTest.addUser(user);

        //when
        boolean exceptedTrue = userServiceTest.existsByEmail(existingEmail);

        //then
        assertThat(exceptedTrue).isTrue();
    }

    @Test
    @DisplayName("Update not existing user by email")
    void itShouldUpdateNotExistingUserByEmail() {
        //given
        String notExistingEmail = "notexisting@email.email";
        User notExistingUser = new User(1L, "firstname", "secondname", "middlename", "country", 1, notExistingEmail);

        //when
        Optional<User> exceptedEmpty = userServiceTest.updateByEmail(notExistingEmail, notExistingUser);

        //then
        assertThat(exceptedEmpty).isEmpty();
    }

    @Test
    @DisplayName("Update existing user by email")
    void itShouldUpdateExistingUserByEmail() {
        //given
        String existingEmail = "existing@email.email";
        User existingUser = new User(1L, "firstname", "secondname", "middlename", "country", 1, existingEmail);
        userServiceTest.addUser(existingUser);
        existingUser.setFirstName("newFirstName");
        existingUser.setLastName("newLastName");
        existingUser.setMiddleName("newMiddleName");
        existingUser.setEmail("newEmail");
        existingUser.setGender(1);

        //when
        Optional<User> exceptedPresent = userServiceTest.updateByEmail(existingEmail, existingUser);

        //then
        assertThat(exceptedPresent).isPresent();
        assertThat(exceptedPresent.get().getFirstName()).isEqualTo(existingUser.getFirstName());
        assertThat(exceptedPresent.get().getLastName()).isEqualTo(existingUser.getLastName());
        assertThat(exceptedPresent.get().getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(exceptedPresent.get().getMiddleName()).isEqualTo(existingUser.getMiddleName());
        assertThat(exceptedPresent.get().getGender()).isEqualTo(existingUser.getGender());
    }

    @Test
    @DisplayName("Update not existing user by id")
    void itShouldUpdateNotExistingUserById() {
        Long notExistingId = 1L;
        User notExistingUser = new User(1L, "firstname", "secondname", "middlename", "country", 1, "notexist@email.email");

        //when
        Optional<User> exceptedEmpty = userServiceTest.updateById(notExistingId, notExistingUser);

        //then
        assertThat(exceptedEmpty).isEmpty();
    }

    @Test
    @DisplayName("Update existing user by id")
    void itShouldUpdateExistingUserById() {
        //given
        Long existingId = 1L;
        User existingUser = new User(1L, "firstname", "secondname", "middlename", "country", 1, "existingEmail@email.email");
        userServiceTest.addUser(existingUser);
        existingUser.setFirstName("newFirstName");
        existingUser.setLastName("newLastName");
        existingUser.setMiddleName("newMiddleName");
        existingUser.setEmail("newEmail");
        existingUser.setGender(1);

        //when
        Optional<User> exceptedPresent = userServiceTest.updateById(existingId, existingUser);

        //then
        assertThat(exceptedPresent).isPresent();
        assertThat(exceptedPresent.get().getFirstName()).isEqualTo(existingUser.getFirstName());
        assertThat(exceptedPresent.get().getLastName()).isEqualTo(existingUser.getLastName());
        assertThat(exceptedPresent.get().getEmail()).isEqualTo(existingUser.getEmail());
        assertThat(exceptedPresent.get().getMiddleName()).isEqualTo(existingUser.getMiddleName());
        assertThat(exceptedPresent.get().getGender()).isEqualTo(existingUser.getGender());
    }
}