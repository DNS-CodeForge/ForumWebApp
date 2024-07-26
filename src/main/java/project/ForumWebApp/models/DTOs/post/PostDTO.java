package project.ForumWebApp.models.DTOs.post;

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
@Schema(description = "DTO representing a post with detailed information")
public class PostDTO {

    @Schema(description = "ID of the post", example = "1")
    private Integer id;

    @Schema(description = "Title of the post", example = "My First Post")
    private String title;

    @Schema(description = "Description of the post", example = "This is the content of my first post")
    private String description;

    @Schema(description = "User who created the post")
    private ApplicationUserDTO user;

    @Schema(description = "Date and time when the post was created", example = "2023-07-21T17:32:28Z")
    private Instant createdDate;

    @Schema(description = "Likes associated with the post")
    private Set<LikeDTO> likes;

    @Schema(description = "Comments associated with the post")
    private Set<CommentDTO> comments;

    @Schema(description = "Tags associated with the post")
    private Set<TagDTO> tags;
}
