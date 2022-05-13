package com.example.usercrud.business.service

import com.example.usercrud.business.entity.User
import com.example.usercrud.persistance.repository.UserRepository
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class UserServiceImplTest extends Specification {
    private static final testUser = new User(1L, "firstName", "lastName", "middleName", "country", 1, "email@email.email")

    def restTemplate = Mock(RestTemplate)
    def userRepository = Mock(UserRepository)

    def "should return false for deleting not existing user by ID"() {
        given:
            userRepository.existsById(1L) >> false
            def userService = new UserServiceImpl(userRepository, restTemplate)
        when:
            def answer = userService.deleteById(1L)
        then:
            !answer
    }

    def "should return true for deleting existing user by ID"() {
        given:
            userRepository.existsById(1L) >> true
            def userService = new UserServiceImpl(userRepository, restTemplate)
        when:
            def answer = userService.deleteById(1L)
        then:
            answer
    }

    def "should return false for deleting not existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> false
            def userService = new UserServiceImpl(userRepository, restTemplate)
        when:
            def answer = userService.deleteByEmail("email@email.email")
        then:
            !answer
    }

    def "should return true for deleting existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> true
            def userService = new UserServiceImpl(userRepository, restTemplate)
            userRepository.save(testUser)
        when:
            def answer = userService.deleteByEmail("email@email.email")
        then:
            answer
    }


    def "should return false for checking if user exists for not existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> false
            def userService = new UserServiceImpl(userRepository, restTemplate)
        when:
            def answer = userService.existsByEmail("email@email.email")
        then:
            !answer
    }

    def "should return true for checking if user exists for existing user by email"() {
        given:
            userRepository.existsByEmail("email@email.email") >> true
            def userService = new UserServiceImpl(userRepository, restTemplate)
            userRepository.save(testUser)
        when:
            def answer = userService.existsByEmail("email@email.email")
        then:
            answer
    }

    def "should update not existing user by email"() {
        given:
            userRepository.findByEmail("email@email.email") >> Optional.empty()
            def userService = new UserServiceImpl(userRepository, restTemplate)
        when:
            def updatedUser = userService.updateByEmail("email@email.email", testUser)
        then:
            updatedUser.isEmpty()
    }


    def "should update existing user by email"() {
        given:
            userRepository.findByEmail("email@email.email") >> Optional.of(testUser)
            def userService = new UserServiceImpl(userRepository, restTemplate)
            def newUser = new User(1L, "newFirstName", "newLastName", "newMiddleName", "newCountry", 2, "newEmail@email.email")
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
            def userService = new UserServiceImpl(userRepository, restTemplate)
        when:
            def updatedUser = userService.updateById(1L, testUser)
        then:
            updatedUser.isEmpty()
    }

    def "should update existing user by id"() {
        given:
            userRepository.findById(1L) >> Optional.of(testUser)
            def userService = new UserServiceImpl(userRepository, restTemplate)
            def newUser = new User(null, "newFirstName", "newLastName", "newMiddleName", "newCountry", 2, "newEmail@email.email")
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
