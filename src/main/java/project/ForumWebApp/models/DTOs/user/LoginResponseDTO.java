package project.ForumWebApp.models.DTOs.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.models.ApplicationUser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private ApplicationUser user;
    private String jwt;
}
