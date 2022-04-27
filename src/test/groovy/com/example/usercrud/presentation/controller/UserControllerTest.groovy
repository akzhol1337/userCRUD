package com.example.usercrud.presentation.controller

import com.example.usercrud.business.entity.User
import com.example.usercrud.business.service.UserService
import com.example.usercrud.persistance.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest extends Specification {
    private static final testUser = new User(null,
        "firstName",
        "lastName",
        "middleName",
        "country",
        1,
        "email@email.email")

    private static ObjectMapper mapper = new ObjectMapper()

    @Shared
    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:latest")

    @Autowired
    private UserService userService

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private UserRepository userRepository

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        if (!postgresContainer.isRunning()) {
            postgresContainer.start()
        }
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl)
        registry.add("spring.datasource.username", postgresContainer::getUsername)
        registry.add("spring.datasource.password", postgresContainer::getPassword)
    }

    def cleanup() {
        userRepository.deleteAll()
    }

    def "should return 200 when adding new user with correct data"() {
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testUser)))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.OK.value()
    }

    def "should return 400 when adding user with empty first name"() {
        given:
            def testIncorrectFirstNameUser = new User(1L,
                null,
                "lastName",
                "middleName",
                "country",
                1,
                "email@email.email")
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testIncorrectFirstNameUser)))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }


    def "should return 400 when adding user with empty email"() {
        given:
            def testIncorrectEmailUser = new User(1L,
                "firstName",
                "lastName",
                "middleName",
                "country",
                1,
                null)
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testIncorrectEmailUser)))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 400 when adding user with incorrect gender"() {
        given:
            def testIncorrectGenderUser = new User(1L,
                "firstName",
                "lastName",
                "middleName",
                "country",
                null,
                "email@email.email")
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testIncorrectGenderUser)))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 400 when adding user with existing email"() {
        given:
            userRepository.save(testUser)
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testUser)))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 200 for getting existing user by id"() {
        given:
            Long id = userService.addUser(testUser).get().getId()
        when:
            def response = mockMvc.perform(get("/user/id/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.OK.value()
    }

    def "should return 400 for getting un existing user by id"() {
        when:
            def response = mockMvc.perform(get("/user/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }

    def "should return 200 for getting existing user by email"() {
        given:
            userRepository.save(testUser)
        when:
            def response = mockMvc.perform(get("/user/email/email@email.email")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.OK.value()
    }

    def "should return 400 for getting un existing user by email"() {
        when:
            def response = mockMvc.perform(get("/user/email/email@email.email")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
    }
}
