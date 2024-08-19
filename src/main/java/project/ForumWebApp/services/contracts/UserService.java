package project.ForumWebApp.services.contracts;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;


public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO);

    RegistrationDTO viewUserInfo();

    ApplicationUser getUserById(int id);

    ApplicationUser getUserByName(String name);

    List<ApplicationUser> getAllUsers();

    void deleteUser(int id);

    ApplicationUser setUserRole(int userId, String addedRole, String removedRole);

    List<ApplicationUser> findUserByUsername(String username);

    Page<ApplicationUser> getAllUsers(Pageable pageable);

    Page<ApplicationUser> findUserByUsername(String username, Pageable pageable);

    Page<ApplicationUser> findUsersByUsernameAndRole(String username, String role, Pageable pageable);

    ApplicationUser findByID(int id);

    void updateUserProfileImage(String imageUrl);
}
