package project.ForumWebApp.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a like")
public class LikeDTO {

    @Schema(description = "ID of the like", example = "1")
    private Integer id;

    @Schema(description = "ID of the user who liked the post", example = "1")
    private Integer userId;
}
