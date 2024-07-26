package project.ForumWebApp.models.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user information")
public class ApplicationUserDTO {

    @Schema(description = "User ID", example = "1")
    private Integer id;

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Size(
            max = ValidationConstants.EMAIL_MAX_LEN,
            message = ValidationConstants.EMAIL_LENGTH_MESSAGE
    )
    @Schema(description = "Email of the user", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.USERNAME_MIN_LEN,
            max = ValidationConstants.USERNAME_MAX_LEN,
            message = ValidationConstants.USERNAME_LENGTH_MESSAGE
    )
    @Schema(description = "Username of the user", example = "john_doe", required = true)
    private String username;
}
