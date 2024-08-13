package project.ForumWebApp.services.contracts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import project.ForumWebApp.models.ApplicationUser;

public interface MvcAuthenticationService extends AuthenticationService {
    ApplicationUser loginUser(String username, String password, HttpServletRequest request, HttpServletResponse response);
    void logoutUser(HttpServletRequest request, HttpServletResponse response);
}
