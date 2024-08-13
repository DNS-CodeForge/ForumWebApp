package project.ForumWebApp.controllers.Rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.ForumWebApp.config.CustomResponse;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.LoginDTO;
import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.services.contracts.RestAuthenticationService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final RestAuthenticationService authenticationService;

    public AuthenticationController(RestAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(schema = @Schema(implementation = ApplicationUser.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<CustomResponse> addUser(
            @Parameter(description = "Registration details") @RequestBody RegistrationDTO registrationDTO) {
        var user = authenticationService.registerUser(registrationDTO);
        var response = new CustomResponse();
        response.getData().put("user", user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Log in a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/login")
    public LoginResponseDTO loginUser(
            @Parameter(description = "Login details") @RequestBody LoginDTO body) {
        var response = authenticationService.loginUser(body.getUsername(), body.getPassword());
        response.getUser().setPassword("*********");
        return response;
    }
}
