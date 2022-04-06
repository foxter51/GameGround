package com.project.GameGround.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserDTO {

    @NotNull
    private Long id;

    @Email
    @NotEmpty
    @Max(32)
    private String email;

    @NotBlank
    @Max(64)
    private String password;

    @NotBlank
    @Max(32)
    private String firstName;

    @Max(32)
    private String lastName;
}
