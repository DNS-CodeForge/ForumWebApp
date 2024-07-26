package project.ForumWebApp.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity representing a role assigned to users")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @Schema(description = "ID of the role", example = "1")
    private Integer roleId;

    @Schema(description = "Authority of the role", example = "ROLE_USER", required = true)
    private String authority;

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    @Schema(description = "Gets the authority of the role", example = "ROLE_USER")
    public String getAuthority() {
        return this.authority;
    }
}
