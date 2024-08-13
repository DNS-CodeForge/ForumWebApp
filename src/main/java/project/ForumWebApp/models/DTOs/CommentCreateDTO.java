package project.ForumWebApp.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for creating a new comment")
public class CommentCreateDTO {

    @Size(
            min = ValidationConstants.COMMENT_MIN_LEN,
            max = ValidationConstants.COMMENT_MAX_LEN,
            message = ValidationConstants.COMMENT_LENGTH_MESSAGE
    )
    @Schema(description = "Content of the comment", example = "This is a comment", required = true)
    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    private String content;
}
