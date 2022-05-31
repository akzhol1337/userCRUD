package com.example.usercrud.business.service

import com.example.usercrud.business.entity.User
import com.example.usercrud.persistance.repository.UserRepositoryHibernate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.client.RestTemplate
import spock.lang.Specification


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceImplTest extends Specification {
    private static final testUser = new User(1L, "firstName", "lastName", "middleName", "country", 1, "email@email.email", null)

    def restTemplate = Mock(RestTemplate)
    def userRepository = Mock(UserRepositoryHibernate)

    @Autowired
    UserService userService;

    def "should return false for deleting not existing user by ID"() {
        given:
            userRepository.existsById(1L) >> false
            userService.setRepository(userRepository)
        when:
            def answer = userService.deleteById(1L)
        then:
            !answer
    }

    def "should return true for deleting existing user by ID"() {
        given:
            userRepository.existsById(1L) >> true
            userService.setRepository(userRepository)
        when:
            def answer = userService.deleteById(1L)
        then:
            answer
    }

    def "should return false for deleting not existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> false
            userService.setRepository(userRepository)
        when:
            def answer = userService.deleteByEmail("email@email.email")
        then:
            !answer
    }

    def "should return true for deleting existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> true
            userService.setRepository(userRepository)
        when:
            def answer = userService.deleteByEmail("email@email.email")
        then:
            answer
    }


    def "should return false for checking if user exists for not existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> false
            userService.setRepository(userRepository)
        when:
            def answer = userService.existsByEmail("email@email.email")
        then:
            !answer
    }

    def "should return true for checking if user exists for existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> true
            userService.setRepository(userRepository)
        when:
            def answer = userService.existsByEmail("email@email.email")
        then:
            answer
    }

    def "should update not existing user by email"() {
        given:
            userRepository.findByEmail("email@email.email") >> Optional.empty()
            userService.setRepository(userRepository)
        when:
            def updatedUser = userService.updateByEmail("email@email.email", testUser)
        then:
            updatedUser.isEmpty()
    }


    def "should update existing user by email"() {
        given:
            userRepository.findByEmail("email@email.email") >> Optional.of(testUser)
            userService.setRepository(userRepository)
            def newUser = new User(1L, "newFirstName", "newLastName", "newMiddleName", "newCountry", 2, "newEmail@email.email", null)
        when:
            def updatedUser = userService.updateByEmail("email@email.email", newUser)
        then:
            updatedUser.isPresent()
            updatedUser.get().getFirstName() == "newFirstName"
            updatedUser.get().getLastName() == "newLastName"
            updatedUser.get().getMiddleName() == "newMiddleName"
            updatedUser.get().getEmail() == "newEmail@email.email"
            updatedUser.get().getCountry() == "newCountry"
            updatedUser.get().getGender() == 2
    }


    def "should update not existing user by id"() {
        given:
            userRepository.findById(1L) >> Optional.empty()
            userService.setRepository(userRepository)
        when:
            def updatedUser = userService.updateById(1L, testUser)
        then:
            updatedUser.isEmpty()
    }

    def "should update existing user by id"() {
        given:
            userRepository.findById(1L) >> Optional.of(testUser)
            userService.setRepository(userRepository)
            def newUser = new User(null, "newFirstName", "newLastName", "newMiddleName", "newCountry", 2, "newEmail@email.email", null)
        when:
            def updatedUser = userService.updateById(1L, newUser)
        then:
            updatedUser.isPresent()
            updatedUser.get().getFirstName() == "newFirstName"
            updatedUser.get().getLastName() == "newLastName"
            updatedUser.get().getMiddleName() == "newMiddleName"
            updatedUser.get().getEmail() == "newEmail@email.email"
            updatedUser.get().getCountry() == "newCountry"
            updatedUser.get().getGender() == 2
    }
}
