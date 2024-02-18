package com.krososs.trains.rest.authentication.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotNull(message = "Username is required.")
    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotNull(message = "Password is required.")
    @Size(min = 5, message= "Password should contain at least 5 characters.")
    private String password;

    @NotNull(message = "Email is required.")
    private String email;
}