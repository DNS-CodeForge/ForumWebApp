package project.ForumWebApp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Entity representing a like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID of the like", example = "1")
    private Integer id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "post_id")
    @Schema(description = "Post that was liked")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "User who liked the post")
    private ApplicationUser user;
}
