package project.ForumWebApp.models.DTOs;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {
    private String title;
    private String description;
    private Set<String> tagNames;
}
