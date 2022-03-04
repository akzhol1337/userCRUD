package com.example.usercrud.Business.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name="id",
            updatable=false
    )
    private Long id;
    @Column(
            name="first_name",
            columnDefinition = "TEXT"
    )
    private String firstName;
    @Column(
            name="last_name",
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name="middle_name",
            columnDefinition = "TEXT"
    )
    private String middleName;
    @Column(
            name="country",
            columnDefinition = "TEXT"
    )
    @NotEmpty(message="Country must not be empty")
    private String country;
    @Column(
            name="gender",
            columnDefinition = "SMALLINT"
    )
    private Short gender;
    @Column(
            name="email",
            columnDefinition = "TEXT"

    )
    private String email;
}
