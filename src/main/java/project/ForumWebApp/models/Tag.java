package project.ForumWebApp.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.TAG_MIN_LEN,
            max = ValidationConstants.TAG_MAX_LEN,
            message = ValidationConstants.TAG_LENGTH_MESSAGE
    )
    private String name;

    @ManyToMany(mappedBy = "tags")
    @JsonBackReference
    private Set<Post> posts = new HashSet<>();

    @PreRemove
    private void removeAssociationsWithPosts() {
        for (Post post : this.posts) {
            post.getTags().remove(this);
        }
        this.posts.clear();
    }
}
