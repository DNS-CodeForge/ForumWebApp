package project.ForumWebApp.services.Implementations;


import java.util.HashSet;
import java.util.Set;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.RegistrationDTO;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.models.DTOs.user.LoginResponseDTO;
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

    public ApplicationUser registerUser(RegistrationDTO registrationDTO){

        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());
        ApplicationUser user = modelMapper.map(registrationDTO, ApplicationUser.class);
        user.setPassword(encodedPassword);
        Role userRole = (Role) roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        user.setAuthorities(authorities);
        return userRepository.save(user);
    }

    public LoginResponseDTO loginUser(String username, String password){

        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenService.generateJwt(auth);

            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);

        } catch(AuthenticationException e){
            return new LoginResponseDTO(null, "");
        }
    }
}
