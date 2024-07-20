package project.ForumWebApp.models.DTOs;

import java.time.Instant;
import java.util.Set;

import lombok.Data;

@Data
public class PostDTO{
    private Integer id;
    private String title;
    private String description;
    private Instant createdDate;
    private Integer userId;
    private Integer subForumId;
    private Set<Integer> commentIds;
    private Set<Integer> tagIds;
}
