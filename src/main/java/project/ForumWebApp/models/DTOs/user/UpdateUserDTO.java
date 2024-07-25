package project.ForumWebApp.models.DTOs.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;
import project.ForumWebApp.constants.ValidationConstants;

@Data
public class UpdateUserDTO {
    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LEN,
            message = ValidationConstants.PASSWORD_LENGTH_MESSAGE
    )
    private String password;

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
}
