package com.example.usercrud.business.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private Long id;
    @NotEmpty(message = "First name should not be empty")
    private String firstName;
    private String lastName;
    private String middleName;
    private String country;
    @NotNull(message = "Gender should not be null")
    private Integer gender;
    @NotEmpty(message = "Email should not be empty")
    private String email;
    private MultipartFile avatar;
}
