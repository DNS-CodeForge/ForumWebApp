package project.ForumWebApp.services.contracts;

import project.ForumWebApp.models.ApplicationUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MvcAuthenticationService extends AuthenticationService {
    ApplicationUser loginUser(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
