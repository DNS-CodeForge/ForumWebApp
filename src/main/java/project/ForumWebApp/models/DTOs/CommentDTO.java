package project.ForumWebApp.models.DTOs;

import io.swagger.v3.oas.annotations.Hidden;
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

public class CommentDTO {
    private Integer id;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.COMMENT_MIN_LEN,
            max = ValidationConstants.COMMENT_MAX_LEN,
            message = ValidationConstants.COMMENT_LENGTH_MESSAGE
    )
    private String content;

    private Integer postId;
}
