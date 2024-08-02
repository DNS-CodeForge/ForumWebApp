package project.ForumWebApp.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Entity representing a post in the forum")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID of the post", example = "1")
    private Integer id;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.TITLE_MIN_LENGTH,
            max = ValidationConstants.TITLE_MAX_LENGTH,
            message = ValidationConstants.TITLE_LENGTH_MESSAGE
    )
    @Schema(description = "Title of the post", example = "Introduction to Java", required = true)
    private String title;

    @NotBlank(message = ValidationConstants.NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.DESCRIPTION_MIN_LENGTH,
            max = ValidationConstants.DESCRIPTION_MAX_LENGTH,
            message = ValidationConstants.DESCRIPTION_LENGTH_MESSAGE
    )
    @Schema(description = "Description of the post", example = "This post provides an introduction to Java programming.", required = true)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "User who created the post")
    private ApplicationUser user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Schema(description = "Creation date of the post", example = "2024-07-26T15:30:00Z")
    private Instant createdDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Likes associated with the post")
    private Set<Like> likes = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Comments associated with the post")
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "post_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns =  {@JoinColumn(name = "tag_id")}
    )
    @JsonManagedReference
    @Schema(description = "Tags associated with the post")
    private Set<Tag> tags = new HashSet<>();
}
