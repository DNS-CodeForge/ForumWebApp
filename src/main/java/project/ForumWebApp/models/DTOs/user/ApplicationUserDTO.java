package project.ForumWebApp.models.DTOs.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserDTO {
    private Integer id;

    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Size(
            max = ValidationConstants.EMAIL_MAX_LEN,
            message = ValidationConstants.EMAIL_LENGTH_MESSAGE
    )
    private String email;

    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.USERNAME_MIN_LEN,
            max = ValidationConstants.USERNAME_MAX_LEN,
            message = ValidationConstants.USERNAME_LENGTH_MESSAGE
    )
    private String username;
}
