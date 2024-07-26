package project.ForumWebApp.models.DTOs.post;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.models.DTOs.LikeDTO;
import project.ForumWebApp.models.DTOs.TagDTO;
import project.ForumWebApp.models.DTOs.user.ApplicationUserDTO;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostDTO {
    private Integer id;
    private String title;
    private String description;
    private ApplicationUserDTO user;
    private Instant createdDate;
    private Set<LikeDTO> likes;
    private Set<CommentDTO> comments;
    private Set<TagDTO> tags;
}
