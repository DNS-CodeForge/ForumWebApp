package project.ForumWebApp.controllers;




import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.LoginDTO;
import project.ForumWebApp.models.DTOs.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.RegistrationDTO;
import project.ForumWebApp.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*") 
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ApplicationUser addUser(@RequestBody RegistrationDTO registrationDTO) {
        return authenticationService.registerUser(registrationDTO.getFirstName(), registrationDTO.getLastName(),registrationDTO.getEmail(), registrationDTO.getPassword(), registrationDTO.getUsername());
    }
    @PostMapping("/login")
    public LoginResponseDTO loginUser(@RequestBody LoginDTO body){
        return authenticationService.loginUser(body.getUsername(), body.getPassword());
    }
}
