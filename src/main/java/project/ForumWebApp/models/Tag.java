package project.ForumWebApp.models;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Entity representing a tag that can be associated with posts")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID of the tag", example = "1")
    private int id;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.TAG_MIN_LEN,
            max = ValidationConstants.TAG_MAX_LEN,
            message = ValidationConstants.TAG_LENGTH_MESSAGE
    )
    @Schema(description = "Name of the tag", example = "Programming", required = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    @Schema(description = "Posts associated with the tag")
    private Set<Post> posts = new HashSet<>();

    @PreRemove
    @Schema(description = "Removes associations with posts before the tag is deleted")
    private void removeAssociationsWithPosts() {
        for (Post post : this.posts) {
            post.getTags().remove(this);
        }
        this.posts.clear();
    }
}

