package project.ForumWebApp.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a comment")
public class CommentDTO {

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

    @Schema(description = "ID of the post this comment is associated with", example = "1")
    private Integer postId;
}
