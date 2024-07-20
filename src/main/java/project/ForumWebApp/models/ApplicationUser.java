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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationUser implements UserDetails {

    static final int FIRST_NAME_MAX_LEN = 32;
    static final int LAST_NAME_MAX_LEN = 32;

    static final int FIRST_NAME_MIN_LEN = 4;
    static final int LAST_NAME_MIN_LEN = 4;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    @Size(min = FIRST_NAME_MIN_LEN, max = FIRST_NAME_MAX_LEN, message = "First name must be between {min} and {max} characters.")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = LAST_NAME_MIN_LEN, max = LAST_NAME_MAX_LEN, message = "Last name must be between {min} and {max} characters.")
    private String lastName;



    @Column(unique = true)
    @NotBlank(message = "Email is mandatory")
    @Size(max = 255, message = "Email should not be more than 255 characters")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @Column(unique = true)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities = new HashSet<>();

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
