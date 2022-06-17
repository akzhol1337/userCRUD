package cloud.folium.usercrud.unit

import cloud.folium.usercrud.business.entity.User
import cloud.folium.usercrud.business.service.UserService
import cloud.folium.usercrud.persistance.repository.UserRepositoryHibernate
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    lateinit var userService: UserService

    val testUser = User(1L, "firstName", "lastName", "middleName", "country", 1, "email@email.email", null)
    val testNewUser = User(1L, "newFirstName", "newLastName", "newMiddleName", "newCountry", 2, "newEmail@email.email", null)
    @MockK
    val userRepository = mockk<UserRepositoryHibernate>()

    init {
        /*
        given ("Repository returns false for existsById()") {
            every {
                userRepository.existsById(any())
            } returns false
            userService.setRepository(userRepository)
            `when` ("calling service to delete not existing user by ID") {
                then ("should return false") {
                    userService.deleteById(1L) shouldBe false
                }
            }
        }

        given("Repository returns true for existsById()") {
            every {
                userRepository.existsById(any())
            } returns true
            userService.setRepository(userRepository)
            `when` ("calling service to delete existing user by ID") {
                then ("should return true") {
                    userService.deleteById(1L) shouldBe true
                }
            }
        }

         */

        given ("Repository returns false for existsByEmail()") {
            every {
                userRepository.existsByEmail(any())
            } returns false
            userService.setRepository(userRepository)
            `when` ("calling service to delete user by Email") {
                then ("should return false") {
                    userService.deleteByEmail("email@email.email") shouldBe false
                }
            }
        }

        given("Repository returns true for existsByEmail()") {
            every {
                userRepository.existsByEmail(any())
            } returns true
            every {
                userRepository.deleteByEmail(any())
            } just Runs
            userService.setRepository(userRepository)
            `when` ("calling service to delete user by Email") {
                then ("should return true") {
                    userService.deleteByEmail("email@email.email") shouldBe true
                }
            }
        }

        given("Repository returns true for existsByEmail()") {
            every {
                userRepository.existsByEmail(any())
            } returns true
            userService.setRepository(userRepository)
            `when` ("calling service to check if user exists") {
                then ("should return true") {
                    userService.existsByEmail("email@email.email") shouldBe true
                }
            }
        }

        given("Repository returns false for existsByEmail()") {
            every {
                userRepository.existsByEmail(any())
            } returns false
            userService.setRepository(userRepository)
            `when` ("calling service to check if user exists") {
                then ("should return false") {
                    userService.existsByEmail("email@email.email") shouldBe false
                }
            }
        }

        given("Repository returns null for findByEmail()") {
            every {
                userRepository.findByEmail(any())
            } returns null
            userService.setRepository(userRepository)
            `when` ("update user") {
                then ("should return null") {
                    userService.updateByEmail("email@email.email", testUser) shouldBe null
                }
            }
        }

        given("Repository returns testUser for findByEmail()") {
            every {
                userRepository.findByEmail(any())
            } returns testUser
            userService.setRepository(userRepository)
            `when` ("update user") {
                val updatedUser = userService.updateByEmail("email@email.email", testNewUser)
                then ("received object's fields should match with sent user object fields") {
                    updatedUser shouldNotBe null
                    updatedUser?.apply {
                        firstName shouldBe "newFirstName"
                        lastName shouldBe "newLastName"
                        middleName shouldBe "newMiddleName"
                        email shouldBe "newEmail@email.email"
                        country shouldBe "newCountry"
                        gender shouldBe 2
                    }
                }
            }
        }

        given("Repository return null for findByEmail()") {
            every {
                userRepository.findByEmail(any())
            } returns null
            `when` ("update user") {
                val updatedUser = userService.updateByEmail("email@email.email", testNewUser)
                then("received object should be null") {
                    updatedUser shouldBe null
                }
            }
        }
/*
        given("Repository returns testUser for findById()") {
            every {
                userRepository.findById(any())
            } returns testUser
            userService.setRepository(userRepository)
            `when` ("update user") {
                val updatedUser = userService.updateById(1L, testNewUser)
                then ("received object's fields should match with sent user object fields") {
                    updatedUser shouldNotBe null
                    updatedUser?.apply {
                        firstName shouldBe "newFirstName"
                        lastName shouldBe "newLastName"
                        middleName shouldBe "newMiddleName"
                        email shouldBe "newEmail@email.email"
                        country shouldBe "newCountry"
                        gender shouldBe 2
                    }
                }
            }
        }

        given("Repository return null for findById()") {
            every {
                userRepository.findById(any())
            } returns null
            `when` ("update user") {
                val updatedUser = userService.updateById(1L, testNewUser)
                then("received object should be null") {
                    updatedUser shouldBe null
                }
            }
        }

 */
    }

}

