package project.ForumWebApp.models.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.TAG_MIN_LEN,
            max = ValidationConstants.TAG_MAX_LEN,
            message = ValidationConstants.TAG_LENGTH_MESSAGE
    )
    private String name;
}
