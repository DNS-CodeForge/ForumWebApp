package project.ForumWebApp.models.DTOs.post;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.models.DTOs.TagDTO;
import project.ForumWebApp.models.DTOs.user.ApplicationUserDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class PostSummaryDTO {
    private Integer id;
    private String title;
    private String description;
    private ApplicationUserDTO user;
    private Instant createdDate;
    private Integer likeCount;
    private Integer commentCount;
    private Set<TagDTO> tags;
    private boolean likedByCurrentUser;


    public String getTimeElapsed() {
        Duration duration = Duration.between(createdDate, Instant.now());

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toHours() < 1) {
            return duration.toMinutes() + " minutes ago";
        } else if (duration.toDays() < 1) {
            return duration.toHours() + " hours ago";
        } else {
            return duration.toDays() + " days ago";
        }
    }
}
