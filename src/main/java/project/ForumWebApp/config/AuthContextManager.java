package project.ForumWebApp.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import project.ForumWebApp.models.ApplicationUser;

import project.ForumWebApp.repository.UserRepository;



@Component
public class AuthContextManager {

    private final UserRepository userRepository;

    public AuthContextManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ApplicationUser getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    public Integer getId() {
        ApplicationUser user = getLoggedInUser();
        return user != null ? user.getId() : null;
    }

    public String getFirstName() {
        ApplicationUser user = getLoggedInUser();
        return user != null ? user.getFirstName() : null;
    }

    public String getLastName() {
        ApplicationUser user = getLoggedInUser();
        return user != null ? user.getLastName() : null;
    }

    public String getEmail() {
        ApplicationUser user = getLoggedInUser();
        return user != null ? user.getEmail() : null;
    }

    public String getUsername() {
        ApplicationUser user = getLoggedInUser();
        return user != null ? user.getUsername() : null;
    }
}
