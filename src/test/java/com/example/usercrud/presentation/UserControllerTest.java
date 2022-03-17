package com.example.usercrud.presentation;

import com.example.usercrud.business.entity.User;
import com.example.usercrud.business.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Adding new user with correct data")
    void itShouldSuccessfullyAddUser() throws Exception {
        User user = new User(null, "firstname", "secondname", "middlename", "country", 0, "email@email.email");
        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Adding new user with incorrect empty firstname")
    void itShouldAddUserWithEmptyFirstname() throws Exception {
        User user = new User(null, null, "secondname", "middlename", "country", 0, "email@email.email");
        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Adding new user with incorrect empty email")
    void itShouldAddUserWithEmptyEmail() throws Exception {
        User user = new User(null, "firstName", "secondname", "middlename", "country", 0, null);
        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Adding new user with incorrect empty gender")
    void itShouldAddUserWithEmptyGender() throws Exception {
        User user = new User(null, "firstNAme", "secondname", "middlename", "country", null, "email@email.email");
        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Adding new user with existing email")
    void itShouldAddUserWithExistingEmail() throws Exception {
        User user = new User(null, "firstName", "secondname", "middlename", "country", 0, "email@email.email");
        userService.addUser(user);
        this.mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Getting existing user by id")
    void itShouldGetExistingUserByID() throws Exception {
        User newUser = new User(null, "firstName", "secondname", "middlename", "country", 0, "email@email.email");
        Optional<User> user = userService.addUser(newUser);
        this.mockMvc.perform(get("/user/id/" + user.get().getId())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting not existing user by id")
    void itShouldGetNotExistingUserByID() throws Exception {
        this.mockMvc.perform(get("/user/id/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Getting existing user by email")
    void itShouldGetExistingUserByEmail() throws Exception {
        User newUser = new User(null, "firstName", "secondname", "middlename", "country", 0, "email@email.email");
        User user = userService.addUser(newUser).get();
        this.mockMvc.perform(get("/user/email/" + user.getEmail())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Getting not existing user by email")
    void itShouldGetNotExistingUserByEmail() throws Exception {
        this.mockMvc.perform(get("/user/email/email@email.email")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete existing user by id")
    void itShouldDeleteExistingUserById() throws Exception {
        User newUser = new User(null, "firstName", "secondname", "middlename", "country", 0, "email@email.email");
        User user = userService.addUser(newUser).get();
        this.mockMvc.perform(delete("/user/id/" + user.getId())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete not existing user by id")
    void itShouldDeleteNotExistingUserById() throws Exception {
        this.mockMvc.perform(delete("/user/id/1")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete existing user by email")
    void itShouldDeleteExistingUserByEmail() throws Exception {
        User newUser = new User(null, "firstName", "secondname", "middlename", "country", 0, "email@email.email");
        User user = userService.addUser(newUser).get();
        this.mockMvc.perform(delete("/user/email/" + user.getEmail())).andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete not existing user by email")
    void itShouldDeleteNotExistingUserByEmail() throws Exception {
        this.mockMvc.perform(delete("/user/email/email@email.email")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update existing user by id")
    void itShouldUpdateExistingUserById() throws Exception {
        User newUser = new User(1L, "firstName", "secondname", "middlename", "country", 1, "email@email.email");
        User user = userService.addUser(newUser).get();
        user.setFirstName("newFirstName");
        user.setLastName("newLastName");
        user.setGender(1);
        user.setEmail("newemail@email.email");
        user.setMiddleName("newMiddleName");
        MvcResult result = this.mockMvc.perform(put("/user/id/" + newUser.getId()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(content, Map.class);

        assertThat(map.get("firstName")).isEqualTo(user.getFirstName());
        assertThat(map.get("lastName")).isEqualTo(user.getLastName());
        assertThat(map.get("middleName")).isEqualTo(user.getMiddleName());
        assertThat(map.get("gender")).isEqualTo((int)user.getGender());
        assertThat(map.get("email")).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Update not existing user by id")
    void itShouldUpdateNotExistingUserById() throws Exception {
        User user = new User();
        user.setFirstName("newFirstName");
        user.setLastName("newLastName");
        user.setGender(1);
        user.setEmail("newemail@email.email");
        user.setMiddleName("newMiddleName");
        this.mockMvc.perform(put("/user/id/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update existing user by email")
    void itShouldUpdateExistingUserByEmail() throws Exception {
        User newUser = new User(1L, "firstName", "secondname", "middlename", "country", 1, "email@email.email");
        User user = userService.addUser(newUser).get();
        user.setFirstName("newFirstName");
        user.setLastName("newLastName");
        user.setGender(1);
        user.setEmail("newemail@email.email");
        user.setMiddleName("newMiddleName");
        MvcResult result = this.mockMvc.perform(put("/user/email/" + newUser.getEmail()).contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(content, Map.class);

        assertThat(map.get("firstName")).isEqualTo(user.getFirstName());
        assertThat(map.get("lastName")).isEqualTo(user.getLastName());
        assertThat(map.get("middleName")).isEqualTo(user.getMiddleName());
        assertThat(map.get("gender")).isEqualTo((int)user.getGender());
        assertThat(map.get("email")).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("Update not existing user by email")
    void itShouldUpdateNotExistingUserByEmail() throws Exception {
        User user = new User();
        user.setFirstName("newFirstName");
        user.setLastName("newLastName");
        user.setGender(1);
        user.setEmail("newemail@email.email");
        user.setMiddleName("newMiddleName");
        this.mockMvc.perform(put("/user/email/email@email.email").contentType(MediaType.APPLICATION_JSON).content(asJsonString(user))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Empty list of users by country")
    void itShouldGetEmptyListOfUsersByCountry() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/users/someCountryName/")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @Test
    @DisplayName("List of users of by country")
    void itShouldGetListOfUsersByCountry() throws Exception {
        User newUser = new User(null, "firstName", "secondname", "middlename", "someCountryName", 1, "email@email.email");
        userService.addUser(newUser);
        MvcResult result = this.mockMvc.perform(get("/users/someCountryName/")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[{\"id\":1,\"firstName\":\"firstName\",\"lastName\":\"secondname\",\"middleName\":\"middlename\",\"country\":\"someCountryName\",\"gender\":1,\"email\":\"email@email.email\"}]");
    }

    @Test
    @DisplayName("Empty list of users")
    void itShouldGetEmptyListOfUsers() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/users/")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @Test
    @DisplayName("List of users")
    void itShouldGetListOfUsers() throws Exception {
        User newUser = new User(null, "firstName", "secondname", "middlename", "someCountryName", 1, "email@email.email");
        userService.addUser(newUser);
        MvcResult result = this.mockMvc.perform(get("/users")).andExpect(status().isOk()).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[{\"id\":1,\"firstName\":\"firstName\",\"lastName\":\"secondname\",\"middleName\":\"middlename\",\"country\":\"someCountryName\",\"gender\":1,\"email\":\"email@email.email\"}]");
    }


}