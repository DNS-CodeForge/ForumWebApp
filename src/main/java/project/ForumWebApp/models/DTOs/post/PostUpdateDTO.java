package project.ForumWebApp.models.DTOs.post;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostUpdateDTO {
    private String title;
    private String description;
    private List<String> tags;
}
