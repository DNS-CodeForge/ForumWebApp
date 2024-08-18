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
import project.ForumWebApp.constants.ValidationConstants;
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
    public String login(HttpServletRequest request, Model model) {
        if (isAuthenticated()) {
            return "redirect:/home";
        }


        String fromRegister = request.getParameter("fromRegister");

        if (fromRegister != null && fromRegister.equals("true")) {
            model.addAttribute("message", "Account registered successfully. Please login here.");
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
        String photo = ValidationConstants.DEFAULT_PHOTO_URL;
        try {
//            model.addAttribute("messege", "Account registered successfully. Please login here.");
//            mvcAuthenticationService.registerUser(new RegistrationDTO(firstName, lastName, email, password, username, photo));
            return "redirect:/login?fromRegister=true";
        } catch (Exception e) {
            return "register";
        }
    }
}
