package project.ForumWebApp.models.DTOs.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.models.DTOs.TagDTO;
import project.ForumWebApp.models.DTOs.user.ApplicationUserDTO;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryDTO {
    private Integer id;
    private String title;
    private String description;
    private ApplicationUserDTO user;
    private Instant createdDate;
    private Integer likeCount;
    private Integer commentCount;
    private Set<TagDTO> tags;
}
