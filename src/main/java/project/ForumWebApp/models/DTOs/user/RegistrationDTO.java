package project.ForumWebApp.models.DTOs.user;

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
public class RegistrationDTO {

    @NotBlank(message = ValidationConstants.FIRST_NAME_LENGTH_MESSAGE)
    @Size(
            min = ValidationConstants.FIRST_NAME_MIN_LEN,
            max = ValidationConstants.FIRST_NAME_MAX_LEN,
            message = ValidationConstants.FIRST_NAME_LENGTH_MESSAGE
    )
    private String firstName;

    @NotBlank(message = ValidationConstants.LAST_NAME_LENGTH_MESSAGE)
    @Size(
            min = ValidationConstants.LAST_NAME_MIN_LEN,
            max = ValidationConstants.LAST_NAME_MAX_LEN,
            message = ValidationConstants.LAST_NAME_LENGTH_MESSAGE
    )
    private String lastName;

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Email(message = ValidationConstants.EMAIL_VALID_MESSAGE)
    @Size(
            max = ValidationConstants.EMAIL_MAX_LEN,
            message = ValidationConstants.EMAIL_LENGTH_MESSAGE
    )
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LEN,
            message = ValidationConstants.PASSWORD_LENGTH_MESSAGE
    )
    private String password;

    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.USERNAME_MIN_LEN,
            max = ValidationConstants.USERNAME_MAX_LEN,
            message = ValidationConstants.USERNAME_LENGTH_MESSAGE
    )
    private String username;

    @Builder.Default
    private String photoUrl = ValidationConstants.DEFAULT_PHOTO_URL;
}
