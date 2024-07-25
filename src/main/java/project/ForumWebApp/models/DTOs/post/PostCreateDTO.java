package project.ForumWebApp.models.DTOs.post;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDTO {
    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.TITLE_MIN_LENGTH,
            max = ValidationConstants.TITLE_MAX_LENGTH,
            message = ValidationConstants.TITLE_LENGTH_MESSAGE
    )
    private String title;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.DESCRIPTION_MIN_LENGTH,
            max = ValidationConstants.DESCRIPTION_MAX_LENGTH,
            message = ValidationConstants.DESCRIPTION_LENGTH_MESSAGE
    )
    private String description;

    private Set<String> tagNames;
}
