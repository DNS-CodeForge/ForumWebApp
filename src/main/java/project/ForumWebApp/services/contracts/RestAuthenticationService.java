package project.ForumWebApp.services.contracts;


import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;


public interface RestAuthenticationService extends AuthenticationService {
    LoginResponseDTO loginUser(String username, String password);
}
