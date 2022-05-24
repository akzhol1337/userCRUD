package com.example.usercrud.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
        name = "id",
        updatable = false
    )
    private Long id;
    @Column(
        name = "first_name",
        columnDefinition = "TEXT"
    )
    @NotEmpty(message = "First name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String firstName;
    @Column(
        name = "last_name",
        columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
        name = "middle_name",
        columnDefinition = "TEXT"
    )
    private String middleName;
    @Column(
        name = "country",
        columnDefinition = "TEXT"
    )
    private String country;
    @Column(
        name = "gender",
        columnDefinition = "SMALLINT"
    )
    @NotNull
    @Min(0)
    @Max(10)
    private Integer gender;
    @Column(
        name = "email",
        updatable = false,
        unique = true
    )
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;


    private String avatar;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;

        return Objects.equals(user.getFirstName(), firstName)
            && Objects.equals(user.getLastName(), lastName)
            && Objects.equals(user.getMiddleName(), middleName)
            && Objects.equals(user.getCountry(), country)
            && Objects.equals(user.getGender(), gender)
            && Objects.equals(user.getEmail(), email);
    }

    public User(UserRequestDto userRequestDto) {
        firstName = userRequestDto.getFirstName();
        lastName = userRequestDto.getLastName();
        middleName = userRequestDto.getMiddleName();
        gender = userRequestDto.getGender();
        email = userRequestDto.getEmail();
    }
}
