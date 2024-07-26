package project.ForumWebApp.models.DTOs.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.models.ApplicationUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for the response after a user logs in")
public class LoginResponseDTO {

    @Schema(description = "The logged-in user", example = "{\"username\":\"john_doe\",\"email\":\"john.doe@example.com\"}")
    private ApplicationUser user;

    @Schema(description = "JWT token for the session", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String jwt;
}
