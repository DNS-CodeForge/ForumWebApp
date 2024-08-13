package project.ForumWebApp.models.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@Schema(name = "User Update", description = "DTO for updating user information")
public class UpdateUserDTO {
    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LEN,
            message = ValidationConstants.PASSWORD_LENGTH_MESSAGE
    )
    @Schema(description = "Password of the user", example = "newpassword123", required = true)
    private String password;

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

    @NotBlank(message = ValidationConstants.PHOTO_URL_NOT_BLANK_MESSAGE)
    @Size(
            max = ValidationConstants.PHOTO_URL_MAX_LEN,
            message = ValidationConstants.PHOTO_URL_LENGTH_MESSAGE
    )
    @Schema(description = "Photo URL of the user", example = "https://example.com/photo.jpg", required = true)
    private String photoUrl;
}
