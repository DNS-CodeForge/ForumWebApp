package project.ForumWebApp.models.DTOs.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    private String username;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    private String password;
}
