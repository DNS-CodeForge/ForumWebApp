package project.ForumWebApp.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.ForumWebApp.constants.ValidationConstants;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "Entity representing a user in the forum web application")
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "ID of the user", example = "1")
    private Integer id;

    @Column(name = "first_name")
    @Size(
            min = ValidationConstants.FIRST_NAME_MIN_LEN,
            max = ValidationConstants.FIRST_NAME_MAX_LEN,
            message = ValidationConstants.FIRST_NAME_LENGTH_MESSAGE
    )
    @Schema(description = "First name of the user", example = "John")
    private String firstName;

    @Column(name = "last_name")
    @Size(
            min = ValidationConstants.LAST_NAME_MIN_LEN,
            max = ValidationConstants.LAST_NAME_MAX_LEN,
            message = ValidationConstants.LAST_NAME_LENGTH_MESSAGE
    )
    @Schema(description = "Last name of the user", example = "Doe")
    private String lastName;

    @Column(unique = true)
    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Size(
            max = ValidationConstants.EMAIL_MAX_LEN,
            message = ValidationConstants.EMAIL_LENGTH_MESSAGE
    )
    @EqualsAndHashCode.Include
    @Schema(description = "Email of the user", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LEN,
            message = ValidationConstants.PASSWORD_LENGTH_MESSAGE
    )
    @Schema(description = "Password of the user", example = "password123", required = true)
    @JsonIgnore
    private String password;

    @Column(unique = true)
    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.USERNAME_MIN_LEN,
            max = ValidationConstants.USERNAME_MAX_LEN,
            message = ValidationConstants.USERNAME_LENGTH_MESSAGE
    )
    @EqualsAndHashCode.Include
    @Schema(description = "Username of the user", example = "john_doe", required = true)
    private String username;

    @Column(name = "photo_url", nullable = false)
    @Size(
            max = ValidationConstants.PHOTO_URL_MAX_LEN,
            message = ValidationConstants.PHOTO_URL_LENGTH_MESSAGE
    )
    @NotBlank(message = ValidationConstants.PHOTO_URL_NOT_BLANK_MESSAGE)
    @Schema(description = "Photo URL of the user", example = "https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", required = true)
    private String photoUrl = ValidationConstants.DEFAULT_PHOTO_URL;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @Schema(description = "Roles associated with the user")
    private Set<Role> authorities = new HashSet<>();


    public ApplicationUser(
            Integer id, String firstName, String lastName, String email,
            String password, String username, Set<Role> authorities
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.photoUrl = ValidationConstants.DEFAULT_PHOTO_URL;
        this.authorities = authorities;
    }

    @Override
    @Schema(description = "Authorities granted to the user")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    public Set<Role> getAuthoritySet() {
        return this.authorities;
    }

    @Override
    @Schema(description = "Indicates whether the user's account has expired", defaultValue = "true")
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Schema(description = "Indicates whether the user's account is locked", defaultValue = "true")
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Schema(description = "Indicates whether the user's credentials have expired", defaultValue = "true")
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Schema(description = "Indicates whether the user is enabled", defaultValue = "true")
    public boolean isEnabled() {
        return true;
    }
}
