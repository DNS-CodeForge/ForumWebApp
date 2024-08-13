package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.ForumWebApp.constants.ValidationConstants;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.contracts.RestAuthenticationService;
import project.ForumWebApp.services.contracts.TokenService;


@ExtendWith(MockitoExtension.class)
class RestAuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RestAuthenticationService authenticationService;

    private RegistrationDTO registrationDTO;
    private ApplicationUser user;
    private Role role;

    @BeforeEach
    void setUp() {
        registrationDTO = new RegistrationDTO();
        registrationDTO.setUsername("testuser");
        registrationDTO.setPassword("password");
        registrationDTO.setEmail("testuser@example.com");

        user = new ApplicationUser();
        user.setUsername("testuser");
        user.setPassword("encodedPassword");

        role = new Role();
        role.setAuthority("USER");
    }

    @Test
    void registerUser_Success() {
        when(userRepository.findByUsername(registrationDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");
        when(modelMapper.map(registrationDTO, ApplicationUser.class)).thenReturn(user);
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.of(role));
        when(userRepository.save(user)).thenReturn(user);

        ApplicationUser registeredUser = authenticationService.registerUser(registrationDTO);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertTrue(registeredUser.getAuthorities().contains(role));
    }

    @Test
    void registerUser_Fail_DuplicateUsername() {
        when(userRepository.findByUsername(registrationDTO.getUsername())).thenReturn(Optional.of(user));

        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                authenticationService.registerUser(registrationDTO));

        assertEquals(ValidationConstants.NOT_UNIQUE_USERNAME_MESSAGE, exception.getMessage());
    }

    @Test
    void registerUser_Fail_DuplicateEmail() {
        when(userRepository.findByUsername(registrationDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.of(user));

        EntityExistsException exception = assertThrows(EntityExistsException.class, () ->
                authenticationService.registerUser(registrationDTO));

        assertEquals(ValidationConstants.NOT_UNIQUE_EMAIL_MESSAGE, exception.getMessage());
    }

    @Test
    void registerUser_Fail_RoleNotFound() {
        when(userRepository.findByUsername(registrationDTO.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registrationDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");
        when(modelMapper.map(registrationDTO, ApplicationUser.class)).thenReturn(user);
        when(roleRepository.findByAuthority("USER")).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                authenticationService.registerUser(registrationDTO));

        assertEquals(ValidationConstants.ROLE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void loginUser_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(tokenService.generateJwt(any(Authentication.class))).thenReturn("token");

        LoginResponseDTO loginResponse = authenticationService.loginUser("testuser", "password");

        assertNotNull(loginResponse);
        assertEquals(user, loginResponse.getUser());
        assertEquals("token", loginResponse.getJwt());
    }

    @Test
    void loginUser_Fail_InvalidCredentials() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        AuthorizationException exception = assertThrows(AuthorizationException.class, () ->
                authenticationService.loginUser("testuser", "wrongpassword"));

        assertEquals(ValidationConstants.INVALID_USERNAME_OR_PASSWORD, exception.getMessage());
    }
}

