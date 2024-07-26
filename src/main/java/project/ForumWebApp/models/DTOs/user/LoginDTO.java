package project.ForumWebApp.models.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User Login", description = "DTO for logging in a user")
public class LoginDTO {
    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Schema(description = "Username of the user", example = "john_doe", required = true)
    private String username;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Schema(description = "Password of the user", example = "password123", required = true)
    private String password;
}
