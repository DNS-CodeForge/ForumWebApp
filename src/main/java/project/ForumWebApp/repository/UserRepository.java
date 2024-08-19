package project.ForumWebApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Role;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer> {
    Optional<ApplicationUser> findByUsername(String username);
    Optional<ApplicationUser> findByEmail(String email);
    List<ApplicationUser> findByUsernameContaining(String username);
    Page<ApplicationUser> findByUsernameContaining(String username, Pageable pageable);
    @Query("SELECT u FROM ApplicationUser u JOIN u.authorities r WHERE u.username LIKE %:username% AND r.authority LIKE %:role%")
    Page<ApplicationUser> findByUsernameContainingAndAuthorityContaining(@Param("username") String username, @Param("role") String role, Pageable pageable);
    @Query("SELECT u FROM ApplicationUser u JOIN u.authorities r WHERE r.authority LIKE %:role%")
    Page<ApplicationUser> findByAuthorityContaining(@Param("role") String role, Pageable pageable);
}
