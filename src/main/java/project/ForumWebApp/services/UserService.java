package project.ForumWebApp.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;


public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO);
    public RegistrationDTO viewUserInfo();
    public ApplicationUser getUserById(int id);
    public ApplicationUser getUserByName(String name);
    public List<ApplicationUser> getAllUsers();
    public void deleteUser(int id);
    public ApplicationUser setUserRole(int userId, String addedRole, String removedRole);
}
