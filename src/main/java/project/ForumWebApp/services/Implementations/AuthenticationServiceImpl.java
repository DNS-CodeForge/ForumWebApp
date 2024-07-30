package project.ForumWebApp.services.Implementations;

import static project.ForumWebApp.constants.ValidationConstants.INVALID_USERNAME_OR_PASSWORD;
import static project.ForumWebApp.constants.ValidationConstants.ROLE_NOT_FOUND;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.ForumWebApp.constants.ValidationConstants;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.AuthenticationService;
import project.ForumWebApp.services.TokenService;


@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {



    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ApplicationUser registerUser(RegistrationDTO registrationDTO) {
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new EntityExistsException(ValidationConstants.NOT_UNIQUE_USERNAME_MESSAGE);
        }
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new EntityExistsException(ValidationConstants.NOT_UNIQUE_EMAIL_MESSAGE);
        }

        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        ApplicationUser user = modelMapper.map(registrationDTO, ApplicationUser.class);
        user.setPassword(encodedPassword);

        Role userRole = roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new EntityNotFoundException(ROLE_NOT_FOUND));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }

    @Override
    public LoginResponseDTO loginUser(String username, String password) {
            ApplicationUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException(INVALID_USERNAME_OR_PASSWORD));
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(user, token);

        } catch (AuthenticationException e) {
            throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
        }
    }
}
