package project.ForumWebApp.services.contracts;

import org.springframework.security.core.Authentication;

public interface TokenService {
    public String generateJwt(Authentication auth);
}
