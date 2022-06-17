package cloud.folium.usercrud.business.entity

import javax.persistence.*
import javax.validation.constraints.*

@Entity(name = "users")
open class User() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    open var id: Long? = null

    @Column(name = "first_name", columnDefinition = "TEXT")
    @NotEmpty(message = "First name should not be empty")
    @Size(
        min = 2,
        max = 30,
        message = "Name should be between 2 - 30 characters"
    )
    open var firstName: String? = null

    @Column(name = "last_name", columnDefinition = "TEXT")
    open var lastName: String? = null

    @Column(name = "middle_name", columnDefinition = "TEXT")
    open var middleName: String? = null

    @Column(name = "country", columnDefinition = "TEXT")
    open var country: String? = null

    @Column(name = "gender", columnDefinition = "SMALLINT")
    @NotNull
    @Min(0)
    @Max(10)
    open var gender: Int? = null

    @Column(name = "email", updatable = false, unique = true)
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    open var email: String? = null

    open var avatar: String? = null

    constructor(id: Long?, firstName: String?, lastName: String?, middleName: String?, country: String?, gender: Int?, email: String?) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.middleName = middleName
        this.country = country
        this.gender = gender
        this.email = email
    }

    constructor(id: Long?, firstName: String?, lastName: String?, middleName: String?, country: String?, gender: Int?, email: String?, avatar: String?) : this() {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.middleName = middleName
        this.country = country
        this.gender = gender
        this.email = email
        this.avatar = avatar
    }

}