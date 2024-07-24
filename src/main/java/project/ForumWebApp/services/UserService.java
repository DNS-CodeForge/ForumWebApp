package project.ForumWebApp.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import project.ForumWebApp.models.DTOs.UpdateUserDTO;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO);

}
