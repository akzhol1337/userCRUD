package cloud.folium.usercrud.integration

import cloud.folium.usercrud.business.entity.User
import cloud.folium.usercrud.business.service.UserService
import cloud.folium.usercrud.persistance.repository.UserRepository
import cloud.folium.usercrud.persistance.repository.UserRepositoryHibernate
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.mockserver.client.MockServerClient

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.util.Base64Utils
import org.testcontainers.containers.PostgreSQLContainer

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest : BehaviorSpec() {
    companion object {
        val postgresContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
            withDatabaseName("postgres")
            withUsername("admin")
            withPassword("password")
            start()
        }
        @JvmStatic
        @DynamicPropertySource
        fun overrideProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
            registry.add("spring.datasource.password", postgresContainer::getPassword);
            registry.add("spring.datasource.username", postgresContainer::getUsername);
        }
    }
    override fun extensions() = listOf(SpringExtension)
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var mockMvc: MockMvc
    val testUser = User(1L,
        "firstName",
        "lastName",
        "middleName",
        "Kazakhstan", 1,
        "email@email.email",
        null)
    val mapper: ObjectMapper = ObjectMapper()
    val gson: Gson = Gson()

    @Autowired
    lateinit var userRepository: UserRepositoryHibernate

    init {
        given("postgresContainer") {
            then("is started") {
                postgresContainer.isCreated shouldBe true
            }
        }

        afterTest {
            userRepository.deleteAll()
        }

        given ("correct user") {
            `when` ("adding") {
                val response = mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(testUser))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:user".toByteArray())))
                    .andReturn()
                    .response
                val responseUser = gson.fromJson(response.getContentAsString(), User::class.java)
                testUser.id = responseUser.id
                then ("http response be with status code 200, and returned user should match with sent user") {
                    response.apply {
                        status shouldBe HttpStatus.OK.value()
                        contentType shouldBe "application/json"
                    }
                    responseUser shouldBeEqualToComparingFields testUser
                }
            }
        }

        given ("user with empty first name") {
            val incorrectUser = User(1L,
                null,
                "lastName",
                "middleName",
                "Kazakhstan",
                1,
                "email@email.email",
                null)
            `when` ("adding") {
                val response = mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(incorrectUser))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:user".toByteArray())))
                    .andReturn()
                    .response
                val responseBody = gson.fromJson(response.contentAsString, User::class.java)
                then ("response should be with status code 400, and message `First name should not be empty`") {
                    response.apply {
                        status shouldBe HttpStatus.BAD_REQUEST.value()
                        contentType shouldBe "application/json"
                    }
                    responseBody.firstName shouldBe "First name should not be empty"
                }
            }
        }

        given ("user with empty email") {
            val incorrectUser = User(1L,
                "firstName",
                "lastName",
                "middleName",
                "Kazakhstan",
                1,
                null,
                null)
            `when` ("adding") {
                val response = mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(incorrectUser))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:user".toByteArray())))
                    .andReturn()
                    .response
                then ("response should be with status code 400") {
                    response.apply {
                        status shouldBe HttpStatus.BAD_REQUEST.value()
                        contentType shouldBe "application/json"
                    }
                }
            }
        }

        given ("user with empty gender") {
            val incorrectUser = User(1L,
                "firstName",
                "lastName",
                "middleName",
                "Kazakhstan",
                null,
                "email@email.email",
                null)
            `when` ("adding") {
                val response = mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(incorrectUser))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:user".toByteArray())))
                    .andReturn()
                    .response
                then ("response should be with status code 400") {
                    response.apply {
                        status shouldBe HttpStatus.BAD_REQUEST.value()
                        contentType shouldBe "application/json"
                        contentAsString shouldBe "{\"gender\":\"must not be null\"}"
                    }
                }
            }
        }

        given ("user with existing email") {
            userRepository.save(testUser)
            `when` ("adding") {
                val response = mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(testUser))
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("user:user".toByteArray())))
                    .andReturn()
                    .response
                println("hhj " + response.contentAsString)
                then ("response should be with status code 400") {
                    response.apply {
                        status shouldBe HttpStatus.BAD_REQUEST.value()
                        contentType shouldBe "application/json"
                        contentAsString shouldBe "{\"error\":\"User with email email@email.email already exist\"}"
                    }
                }
            }
        }

        given ("existing user") {
            val id: Long? = userService.addUser(testUser)?.id
            `when` ("getting user by id") {
                val response = mockMvc.perform(get("/user/id/" + id)
                    .contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("admin:admin".toByteArray())))
                    .andReturn()
                    .response
                val responseUser = gson.fromJson(response.contentAsString, User::class.java)
                testUser.id = responseUser.id
                then ("response should be with status code 200, and returned user should match with sent user") {
                    response.apply {
                        status shouldBe HttpStatus.OK.value()
                        contentType shouldBe "application/json"
                    }
                    responseUser shouldBeEqualToComparingFields testUser
                }
            }
        }

        given ("unexisting user") {
            `when` ("getting user by id") {
                val response = mockMvc.perform(get("/user/id/1")
                    .contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("admin:admin".toByteArray())))
                    .andReturn()
                    .response
                then ("response should be with status code 400, and error message") {
                    response.apply {
                        status shouldBe HttpStatus.BAD_REQUEST.value()
                        contentType shouldBe "application/json"
                        contentAsString shouldBe "{\"error\":\"User with id 1 doesn't exist\"}"
                    }
                }
            }
        }

        given ("existing user") {
            userService.addUser(testUser)
            `when` ("getting user by email") {
                val response = mockMvc.perform(get("/user/email/email@email.email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("admin:admin".toByteArray())))
                    .andReturn()
                    .response
                val responseUser = gson.fromJson(response.contentAsString, User::class.java)
                testUser.id = responseUser.id
                then ("response should be with status code 200, and returned user should match with sent user") {
                    response.apply {
                        status shouldBe HttpStatus.OK.value()
                        contentType shouldBe "application/json"
                    }
                    responseUser shouldBeEqualToComparingFields testUser
                }
            }
        }

        given ("unexisting user") {
            `when` ("getting user by email") {
                val response = mockMvc.perform(get("/user/email/email@email.email")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("admin:admin".toByteArray())))
                    .andReturn()
                    .response
                then ("response should be with status code 400, and error message") {
                    response.apply {
                        status shouldBe HttpStatus.BAD_REQUEST.value()
                        contentType shouldBe "application/json"
                        contentAsString shouldBe "{\"error\":\"User with email email@email.email doesn't exist\"}"
                    }
                }
            }
        }

    }
}