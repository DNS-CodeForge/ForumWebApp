package project.ForumWebApp.models.DTOs.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationDTO {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "First name is mandatory")
    @Size(min = 4, max = 32, message = "First name must be between 4 and 32 characters")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 4, max = 32, message = "Last name must be between 4 and 32 characters")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email should not be more than 255 characters")
    private String email;
}
