package project.ForumWebApp.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "first_name")
    @Size(
            min = ValidationConstants.FIRST_NAME_MIN_LEN,
            max = ValidationConstants.FIRST_NAME_MAX_LEN,
            message = ValidationConstants.FIRST_NAME_LENGTH_MESSAGE
    )
    private String firstName;

    @Column(name = "last_name")
    @Size(
            min = ValidationConstants.LAST_NAME_MIN_LEN,
            max = ValidationConstants.LAST_NAME_MAX_LEN,
            message = ValidationConstants.LAST_NAME_LENGTH_MESSAGE
    )
    private String lastName;

    @Column(unique = true)
    @NotBlank(message = ValidationConstants.EMAIL_NOT_BLANK_MESSAGE)
    @Size(
            max = ValidationConstants.EMAIL_MAX_LEN,
            message = ValidationConstants.EMAIL_LENGTH_MESSAGE
    )
    @EqualsAndHashCode.Include
    private String email;

    @NotBlank(message = ValidationConstants.PASSWORD_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.PASSWORD_MIN_LEN,
            message = ValidationConstants.PASSWORD_LENGTH_MESSAGE
    )
    private String password;

    @Column(unique = true)
    @NotBlank(message = ValidationConstants.USERNAME_NOT_BLANK_MESSAGE)
    @Size(
            min = ValidationConstants.USERNAME_MIN_LEN,
            max = ValidationConstants.USERNAME_MAX_LEN,
            message = ValidationConstants.USERNAME_LENGTH_MESSAGE
    )
    @EqualsAndHashCode.Include
    private String username;

    @Column(name = "photo_url", nullable = false)
    @Size(
            max = ValidationConstants.PHOTO_URL_MAX_LEN,
            message = ValidationConstants.PHOTO_URL_LENGTH_MESSAGE
    )
    @NotBlank(message = ValidationConstants.PHOTO_URL_NOT_BLANK_MESSAGE)
    private String photoUrl = ValidationConstants.DEFAULT_PHOTO_URL;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities = new HashSet<>();

    // Custom constructor
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
