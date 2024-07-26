package project.ForumWebApp.models.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for registering a new user")
public class RegistrationDTO {

    @NotBlank(message = ValidationConstants.FIRST_NAME_LENGTH_MESSAGE)
    @Size(
            min = ValidationConstants.FIRST_NAME_MIN_LEN,
            max = ValidationConstants.FIRST_NAME_MAX_LEN,
            message = ValidationConstants.FIRST_NAME_LENGTH_MESSAGE
    )
    @Schema(description = "First name of the user", example = "John", required = true)
    private String firstName;

    @NotBlank(message = ValidationConstants.LAST_NAME_LENGTH_MESSAGE)
    @Size(
            min = ValidationConstants.LAST_NAME_MIN_LEN,
            max = ValidationConstants.LAST_NAME_MAX_LEN,
            message = ValidationConstants.LAST_NAME_LENGTH_MESSAGE
    )
    @Schema(description = "Last name of the user", example = "Doe", required = true)
    private String lastName;

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = ValidationConstants.EMAIL_VALID_MESSAGE)
    @Size(
            max = ValidationConstants.EMAIL_MAX_LEN,
            message = ValidationConstants.EMAIL_LENGTH_MESSAGE
    )
    @Schema(description = "Email of the user", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LEN,
            message = ValidationConstants.PASSWORD_LENGTH_MESSAGE
    )
    @Schema(description = "Password of the user", example = "password123", required = true)
    private String password;

    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.USERNAME_MIN_LEN,
            max = ValidationConstants.USERNAME_MAX_LEN,
            message = ValidationConstants.USERNAME_LENGTH_MESSAGE
    )
    @Schema(description = "Username of the user", example = "john_doe", required = true)
    private String username;

    @Builder.Default
    @Schema(description = "Photo URL of the user", example = "https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D")
    private String photoUrl = ValidationConstants.DEFAULT_PHOTO_URL;
}
