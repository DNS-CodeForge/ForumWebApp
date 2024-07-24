package project.ForumWebApp.models.DTOs.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUserDTO {
    private Integer id;
    private String email;
    private String username;
}