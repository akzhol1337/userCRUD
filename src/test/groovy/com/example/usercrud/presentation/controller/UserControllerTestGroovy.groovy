package com.example.usercrud.presentation.controller

import com.example.usercrud.business.entity.User
import com.example.usercrud.business.service.UserService
import com.example.usercrud.persistance.repository.UserRepository
import com.example.usercrud.presentation.UserController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import javax.servlet.http.HttpServletRequest

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [UserController])
class UserControllerTestGroovy extends Specification{
    @Autowired
    MockMvc mockMvc
    @Autowired
    UserService userService

    private testUser = new User(1L, "firstName", "lastName", "middleName", "country", 1, "email@email.email")

    private static asJsonString(Object obj){
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    def "should return 200 when adding new user with correct data"() {
        setup:
        userService.addUser(_ as User, _ as HttpServletRequest) >> Optional.of(testUser)

        when:
        def response = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(testUser))).andReturn().response

        then:
        response.status == HttpStatus.OK.value()
    }

    def "should return 400 when adding user with empty first name"() {
        setup:
        def testIncorrectFirstNameUser = new User(1L, null, "lastName", "middleName","country", 1, "email@email.email")

        when:
        def response = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(testIncorrectFirstNameUser))).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 400 when adding user with empty email"() {
        setup:
        def testIncorrectEmailUser = new User(1L, "firstName", "lastName", "middleName","country", 1, null)

        when:
        def response = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(testIncorrectEmailUser))).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 400 when adding user with incorrect gender"() {
        setup:
        def testIncorrectGenderUser = new User(1L, "firstName", "lastName", "middleName","country", null, "email@email.email")

        when:
        def response = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(testIncorrectGenderUser))).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 400 when adding user with existing email"() {
        setup:
        userService.addUser(_ as User, _ as HttpServletRequest) >> Optional.empty()

        when:
        def response = mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(asJsonString(testUser))).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 200 for getting existing user by id"() {
        setup:
        userService.findById(1L) >> Optional.of(testUser)

        when:
        def response = mockMvc.perform(get("/user/id/1").contentType(MediaType.APPLICATION_JSON)).andReturn().response

        then:
        response.status == HttpStatus.OK.value()
    }

    def "should return 400 for getting un existing user by id"() {
        setup:
        userService.findById(1L) >> Optional.empty()

        when:
        def response = mockMvc.perform(get("/user/id/1").contentType(MediaType.APPLICATION_JSON)).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 200 for getting existing user by email"() {
        setup:
        userService.findByEmail("email@email.email") >> Optional.of(testUser)

        when:
        def response = mockMvc.perform(get("/user/email/email@email.email").contentType(MediaType.APPLICATION_JSON)).andReturn().response

        then:
        response.status == HttpStatus.OK.value()
    }

    def "should return 400 for getting un existing user by email"() {
        setup:
        userService.findByEmail("email@email.email") >> Optional.empty()

        when:
        def response = mockMvc.perform(get("/user/email/email@email.email").contentType(MediaType.APPLICATION_JSON)).andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()
    }

    @TestConfiguration
    static class Config {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()
        @Bean
        UserService userService() {
            return detachedMockFactory.Mock(UserService)
        }
    }

}
