package project.ForumWebApp.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Entity representing a comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID of the comment", example = "1")
    private Integer id;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.COMMENT_MIN_LEN,
            max = ValidationConstants.COMMENT_MAX_LEN,
            message = ValidationConstants.COMMENT_LENGTH_MESSAGE
    )
    @Schema(description = "Content of the comment", example = "This is a comment", required = true)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "User who made the comment")
    private ApplicationUser user;

    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @Schema(description = "Post to which the comment belongs")
    private Post post;

//    @PreRemove
//    private void preRemove() {
//        if (Objects.nonNull(post)) {
//            post.getComments().remove(this);
//        }
//    }
}
