package project.ForumWebApp.services.contracts;

import project.ForumWebApp.models.ApplicationUser;

import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;

public interface AuthenticationService {
    ApplicationUser registerUser(RegistrationDTO registrationDTO);
}
