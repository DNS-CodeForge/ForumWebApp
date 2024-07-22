package project.ForumWebApp.services;

import org.springframework.security.core.Authentication;

public interface TokenService {
    public String generateJwt(Authentication auth);
}
