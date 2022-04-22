import com.example.usercrud.business.service.UserService
import com.example.usercrud.persistance.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class UserServiceTestGroovy extends Specification{
    @Autowired
    UserService userServiceTest
    @Autowired
    UserRepository userRepoTest

    // todo


    def "It should return false for deleting un-existing user by id"() {
        setup:
            def userServiceTest = new UserService()
            def id = -1L
        when:
            Boolean answer = userServiceTest.deleteById(id)
        then:
            !answer
    }

    def "It should return true for deleting existing user by id"() {
        setup:
            def user = new User(1L, "firstName", "secondName", "middleName", "country", 1, "email@emailemail")
            userRepoTest.save(user)
        when:
            Boolean answer = userServiceTest.deleteById(1L)
        then:
            answer
    }

    def "It should return false for deleting un-existing user by email"() {
        setup:
            def userServiceTest = new UserService()
            def email = "email@email.email"
        when:
            Boolean answer = userServiceTest.deleteByEmail(email)
        then:
            !answer
    }

    def "It should return true for deleting existing user by email"() {
        setup:
            def user = new User(1L, "firstName", "secondName", "middleName", "country", 1, "email@email.email")
            userRepoTest.save(user)
        when:
            Boolean answer = userServiceTest.deleteById("email@email.email")
        then:
            answer
    }

    def "It should return false for checking if un-existing user exists by email"() {
        setup:
            def email = "email@email.email"
        when:
            boolean answer = userServiceTest.existsByEmail(email)
        then:
            !answer
    }

    def "It should return true for checking if existing user exists by email"() {
        setup:
            def user = new User(1L, "firstName", "secondName", "middleName", "country", 1, "email@email.email")
            def email = "email@email.email"
        when:
            boolean answer = userServiceTest.existsByEmail(email)
        then:
            answer
    }




}
