package com.example.usercrud.presentation.controller


import com.example.usercrud.business.entity.User
import com.example.usercrud.business.service.UserService
import com.example.usercrud.persistance.repository.UserRepositoryHibernate
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.multipart.MultipartFile
import org.testcontainers.containers.PostgreSQLContainer
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
        "Kazakhstan",
        1,
        "email@email.email",
        null
    )


    private static ObjectMapper mapper = new ObjectMapper()
    private static jsonSlurper = new JsonSlurper()

    @Shared
    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:latest")

    @Autowired
    private UserService userService

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private UserRepositoryHibernate userRepository

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

    @WithMockUser(username = "user", authorities = "create")
    def "should return 200 when adding new user with correct data"() {
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testUser)))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
            def responseUser = mapper.convertValue(responseBody, User)
        then:
            response.status == HttpStatus.OK.value()
            response.getContentType() == "application/json"
            testUser.equals(responseUser)
    }


    @WithMockUser(username = "user", authorities = "get")
    def "should return 200 when adding new user with correct data send by multipart/form-data"() {
        given:
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/test_img.png")
            MultipartFile multipartFile = new MockMultipartFile("avatar", "", "image/jpeg", fileInputStream)
        when:
            def response = mockMvc.perform(MockMvcRequestBuilders.multipart("/user/multipart")
                .file(multipartFile)
                .param("firstName", "akzhol")
                .param("gender", "1")
                .param("email", "test@test.test"))
                .andReturn()
                .response
        then:
            response.status == HttpStatus.OK.value()
            response.getContentType() == "application/json"
    }

    @WithMockUser(username = "user", authorities = "create")
    def "should return 400 when adding user with empty first name"() {
        given:
            def testIncorrectFirstNameUser = new User(1L,
                null,
                "lastName",
                "middleName",
                "country",
                1,
                "email@email.email",
                null
            )
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testIncorrectFirstNameUser)))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
            response.getContentType() == "application/json"
            responseBody.firstName == "First name should not be empty"
    }

    @WithMockUser(username = "user", authorities = "create")
    def "should return 400 when adding user with empty email"() {
        given:
            def testIncorrectEmailUser = new User(1L,
                "firstName",
                "lastName",
                "middleName",
                "country",
                1,
                null,
                null)
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testIncorrectEmailUser)))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
            response.getContentType() == "application/json"
            responseBody.email == "Email should not be empty"
    }

    @WithMockUser(username = "user", authorities = "create")
    def "should return 400 when adding user with incorrect gender"() {
        given:
            def testIncorrectGenderUser = new User(1L,
                "firstName",
                "lastName",
                "middleName",
                "country",
                null,
                "email@email.email",
                null)
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testIncorrectGenderUser)))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
            response.getContentType() == "application/json"
            responseBody.gender == "must not be null"
    }

    @WithMockUser(username = "user", authorities = "create")
    def "should return 400 when adding user with existing email"() {
        given:
            userRepository.save(testUser)
        when:
            def response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testUser)))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
            response.getContentType() == "application/json"
            responseBody.error == "User with email email@email.email already exist"
    }

    @WithMockUser(username = "admin", authorities = "get_user")
    def "should return 200 for getting existing user by id"() {
        given:
            Long id = userService.addUser(testUser).get().getId()
        when:
            def response = mockMvc.perform(get("/user/id/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
            def responseUser = mapper.convertValue(responseBody, User)
        then:
            response.status == HttpStatus.OK.value()
            response.getContentType() == "application/json"
            testUser.equals(responseUser)
    }

    @WithMockUser(username = "admin", authorities = "get_user")
    def "should return 400 for getting un existing user by id"() {
        when:
            def response = mockMvc.perform(get("/user/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
            response.getContentType() == "application/json"
            responseBody.error == "User with id 1 doesn't exist"
    }

    @WithMockUser(username = "admin", authorities = "get")
    def "should return 200 for getting existing user by email"() {
        given:
            Long id = userService.addUser(testUser).get().getId()
        when:
            def response = mockMvc.perform(get("/user/email/email@email.email")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
            def responseUser = mapper.convertValue(responseBody, User)
        then:
            response.status == HttpStatus.OK.value()
            response.getContentType() == "application/json"
            testUser.equals(responseUser)
    }

    @WithMockUser(username = "admin", authorities = "get")
    def "should return 400 for getting un existing user by email"() {
        when:
            def response = mockMvc.perform(get("/user/email/email@email.email")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .response
            def responseBody = jsonSlurper.parseText(response.getContentAsString())
        then:
            response.status == HttpStatus.BAD_REQUEST.value()
            response.getContentType() == "application/json"
            responseBody.error == "User with email email@email.email doesn't exist"
    }
}
