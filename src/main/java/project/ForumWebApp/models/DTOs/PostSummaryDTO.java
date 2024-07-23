package project.ForumWebApp.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
