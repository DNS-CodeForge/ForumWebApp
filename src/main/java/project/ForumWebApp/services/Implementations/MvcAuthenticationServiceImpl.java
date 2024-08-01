package project.ForumWebApp.services.Implementations;

import static project.ForumWebApp.constants.ValidationConstants.INVALID_USERNAME_OR_PASSWORD;
import static project.ForumWebApp.constants.ValidationConstants.ROLE_NOT_FOUND;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import project.ForumWebApp.constants.ValidationConstants;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.contracts.MvcAuthenticationService;
import project.ForumWebApp.services.contracts.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@Qualifier("mvcAuthenticationService")
public class MvcAuthenticationServiceImpl implements MvcAuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    @Autowired
    public MvcAuthenticationServiceImpl(
            PasswordEncoder passwordEncoder,
            @Qualifier("mvcAuthenticationManager") AuthenticationManager authenticationManager,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {

        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
        ApplicationUser user = new ApplicationUser();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(encodedPassword);

        Role userRole = roleRepository.findByAuthority("USER")
                .orElseThrow(() -> new EntityNotFoundException(ROLE_NOT_FOUND));

        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        user.setAuthorities(authorities);

        return userRepository.save(user);
    }

    @Override
    public ApplicationUser loginUser(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_USERNAME_OR_PASSWORD));
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            Cookie sessionCookie = new Cookie("SESSIONID", WebUtils.getSessionId(request));
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(true);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);

            return user;

        } catch (AuthenticationException e) {
            throw new AuthorizationException(INVALID_USERNAME_OR_PASSWORD);
        }
    }
}
