package project.ForumWebApp.models.DTOs.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdateUserDTO{
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 characters")
    private String lastName;
}