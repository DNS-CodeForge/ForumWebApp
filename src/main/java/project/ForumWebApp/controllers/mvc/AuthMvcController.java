package project.ForumWebApp.controllers.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.services.contracts.MvcAuthenticationService;

import java.io.IOException;

@Controller
public class AuthMvcController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final MvcAuthenticationService mvcAuthenticationService;

    public AuthMvcController(@Qualifier("mvcAuthenticationManager") AuthenticationManager authenticationManager,
                             AuthenticationFailureHandler authenticationFailureHandler,
                             MvcAuthenticationService mvcAuthenticationService) {
        this.authenticationManager = authenticationManager;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.mvcAuthenticationService = mvcAuthenticationService;
    }

    private String redirectWithAttributes(String targetUrl, RedirectAttributes redirectAttributes, Model model) {
        for (String key : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(key, model.getAttribute(key));
        }
        return "redirect:" + targetUrl;
    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser");
    }

    @GetMapping("/login")
    public String login() {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        if (isAuthenticated()) {
            return "redirect:/home";
        }
        return "register";
    }

    @ModelAttribute("URL")
    public String getURL(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest request, HttpServletResponse response) {
        mvcAuthenticationService.logoutUser(request, response);
        return new RedirectView("/home");
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return "redirect:/home";
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return "login";
        }
    }

    @PostMapping("/register")
    public String register(RedirectAttributes redirectAttributes, Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String email = request.getParameter("email");
        try {
            model.addAttribute("messege", "Account registered successfully. Please login here.");
            mvcAuthenticationService.registerUser(new RegistrationDTO(firstName, lastName, email, password, username, "https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
            return redirectWithAttributes("/login", redirectAttributes, model);
        } catch (Exception e) {
            return "register";
        }
    }
}
