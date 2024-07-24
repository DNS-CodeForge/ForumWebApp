package project.ForumWebApp.models.DTOs.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDTO {

    private static final String DEFAULT_PHOTO_URL = "https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

    @NotBlank(message = "First name is mandatory")
    @Size(min = 4, max = 32, message = "First name must be between {min} and {max} characters.")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 4, max = 32, message = "Last name must be between {min} and {max} characters.")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email should not be more than 255 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 255, message = "Username should not be more than 255 characters")
    private String username;

    @Builder.Default
    private String photoUrl = DEFAULT_PHOTO_URL;
}
