package project.ForumWebApp.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private ApplicationUser user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant createdDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Comment> comments = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "post_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns =  {@JoinColumn(name = "tag_id")}
    )
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();
}
