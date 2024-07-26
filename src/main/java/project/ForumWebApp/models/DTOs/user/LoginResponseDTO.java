package project.ForumWebApp.models.DTOs.user;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.models.ApplicationUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(hidden = true)
@Hidden
public class LoginResponseDTO {
    private ApplicationUser user;
    private String jwt;
}
