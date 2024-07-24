package project.ForumWebApp.services;

import project.ForumWebApp.models.ApplicationUser;
<<<<<<< Updated upstream
import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
=======
import project.ForumWebApp.models.DTOs.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.RegistrationDTO;
>>>>>>> Stashed changes

public interface AuthenticationService {
    ApplicationUser registerUser(RegistrationDTO registrationDTO);
    LoginResponseDTO loginUser(String username, String password);
}
