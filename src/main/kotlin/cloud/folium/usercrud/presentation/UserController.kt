package cloud.folium.usercrud.presentation

import cloud.folium.usercrud.business.entity.User
import org.springframework.beans.factory.annotation.Autowired
import cloud.folium.usercrud.business.service.UserService
import kotlin.Throws
import javax.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import cloud.folium.usercrud.business.entity.UserRequestDto
import cloud.folium.usercrud.business.entity.annotations.Loggable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.lang.Exception
import java.util.*
import javax.validation.Valid

@RestController
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(exception: MethodArgumentNotValidException): MutableMap<String, String?> {
        val errors: MutableMap<String, String?> = mutableMapOf()
        exception.bindingResult.allErrors.forEach{ error ->
            val fieldName: String = (error as FieldError).field
            val errorMessage: String? = error.defaultMessage
            if(!errors.containsKey(fieldName)) {
                errors[fieldName] = errorMessage
            } else {
                errors[fieldName] += " $errorMessage";
            }
        }
        return errors;
    }

    @PostMapping("/user")
    @Throws(Exception::class)
    fun addUser(
        @RequestBody @Valid user: User?,
        request: HttpServletRequest?
    ): ResponseEntity<*> {
        println("abcdefg")
        val newUser = userService.addUser(user, request)
        return if (newUser == null) {
            ResponseEntity.badRequest().body(
                Collections.singletonMap<String, Any>(
                    "error",
                    "User with email " + user!!.email + " already exist"
                )
            )
        } else ResponseEntity.ok(newUser)
    }

    @PostMapping(value = ["/user/multipart"])
    @Throws(IOException::class)
    fun addUser(
        @ModelAttribute userRequestDto: @Valid UserRequestDto?,
        request: HttpServletRequest?
    ): ResponseEntity<*> {
        val newUser = userService.addUser(userRequestDto, request)
        return if (newUser == null) {
            ResponseEntity.badRequest().body(
                Collections.singletonMap<String, Any>(
                    "error",
                    "User with email " + userRequestDto?.email + " already exist"
                )
            )
        } else ResponseEntity.ok(newUser)
    }

    @DeleteMapping("/user/id/{id}")
    fun deleteUserById(@PathVariable id: Long): ResponseEntity<*> {
        return if (userService.deleteById(id)) {
            ResponseEntity.ok(
                Collections.singletonMap(
                    "success",
                    "Deleted user with id $id"
                )
            )
        } else ResponseEntity.badRequest().body(
            Collections.singletonMap(
                "error",
                "User with id $id doesn't exist"
            )
        )
    }

    @GetMapping("/users/{country}")
    fun getAllUsersByCountry(@PathVariable country: String): ResponseEntity<*> {
        return ResponseEntity.ok(userService.getAllByCountry(country))
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<*> {
        return ResponseEntity.ok(userService.getAll())
    }

    @DeleteMapping("/user/email/{email}")
    fun deleteUserByEmail(@PathVariable email: String): ResponseEntity<*> {
        val deleted = userService.deleteByEmail(email)
        return if (deleted) {
            ResponseEntity.ok(
                Collections.singletonMap(
                    "success",
                    "Deleted user with email $email"
                )
            )
        } else ResponseEntity.badRequest().body(
            Collections.singletonMap(
                "error",
                "User with email $email doesn't exist"
            )
        )
    }

    @PutMapping("/user/id/{id}")
    fun updateUserById(@PathVariable id: Long, @RequestBody newUser: User): ResponseEntity<*> {
        val user = userService.updateById(id, newUser)
        return if (user == null) {
            ResponseEntity.badRequest().body(
                Collections.singletonMap(
                    "error",
                    "User with id $id doesn't exist"
                )
            )
        } else ResponseEntity.ok(user)
    }

    @PutMapping("/user/email/{email}")
    fun updateUserByEmail(@PathVariable email: String, @RequestBody newUser: User): ResponseEntity<*> {
        val user = userService.updateByEmail(email, newUser)
        return if (user == null) {
            ResponseEntity.badRequest().body(
                Collections.singletonMap(
                    "error",
                    "User with email $email doesn't exist"
                )
            )
        } else ResponseEntity.ok(user)
    }

    @GetMapping("/user/id/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<*> {
        val user = userService.findById(id)
        return if (user == null) {
            ResponseEntity.badRequest().body(
                Collections.singletonMap(
                    "error",
                    "User with id $id doesn't exist"
                )
            )
        } else ResponseEntity.ok(user)
    }

    @GetMapping("/user/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<*> {
        val user = userService.findByEmail(email)
        return if (user == null) {
            ResponseEntity.badRequest().body(
                Collections.singletonMap(
                    "error",
                    "User with email $email doesn't exist"
                )
            )
        } else ResponseEntity.ok(user)
    }
}