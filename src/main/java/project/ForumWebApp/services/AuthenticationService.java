package project.ForumWebApp.services;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.LoginResponseDTO;

public interface AuthenticationService {
    ApplicationUser registerUser(String firstName, String lastName, String email, String password, String username);
    LoginResponseDTO loginUser(String username, String password);
}
