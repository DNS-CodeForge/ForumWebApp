package project.ForumWebApp.models.DTOs.post;

import java.util.List;

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
@Schema(description = "DTO for updating an existing post")

public class PostUpdateDTO {

    @Schema(description = "ID of the post", example = "1")
    private Integer id;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.TITLE_MIN_LENGTH,
            max = ValidationConstants.TITLE_MAX_LENGTH,
            message = ValidationConstants.TITLE_LENGTH_MESSAGE
    )
    @Schema(description = "Title of the post", example = "Updated Post Title", required = true)
    private String title;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.DESCRIPTION_MIN_LENGTH,
            max = ValidationConstants.DESCRIPTION_MAX_LENGTH,
            message = ValidationConstants.DESCRIPTION_LENGTH_MESSAGE
    )
    @Schema(description = "Description of the post", example = "This is the updated content of my post", required = true)
    private String description;

    @Schema(description = "Tags associated with the post", example = "[\"Spring\", \"Java\"]")
    private List<String> tags;
}
