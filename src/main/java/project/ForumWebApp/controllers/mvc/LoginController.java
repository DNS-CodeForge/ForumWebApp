package project.ForumWebApp.controllers.mvc;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.services.contracts.MvcAuthenticationService;
import project.ForumWebApp.services.contracts.PostService;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final PostService postService;
    private final MvcAuthenticationService mvcAuthenticationService; 

    public LoginController(@Qualifier("mvcAuthenticationManager") AuthenticationManager authenticationManager,
                           AuthenticationFailureHandler authenticationFailureHandler, PostService postService, MvcAuthenticationService mvcAuthenticationService) {
        this.authenticationManager = authenticationManager;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.postService = postService;
        this.mvcAuthenticationService = mvcAuthenticationService;
    }

    private String redirectWithAttributes(String targetUrl, RedirectAttributes redirectAttributes, Model model) {
        for (String key : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(key, model.getAttribute(key));
        }

        return "redirect:" + targetUrl;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
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
            return "redirect:/home";
        } catch (AuthenticationException e) {
            authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            return "login";
        }
    }

    @PostMapping("/register")
    public String register(RedirectAttributes redirectAttributes,Model model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String firstName = request.getParameter("first-name");
        String lastName = request.getParameter("last-name");
        String email = request.getParameter("email");
        try {

            model.addAttribute("messege","Account registered successfully. Please login here.");

            mvcAuthenticationService.registerUser(new RegistrationDTO(firstName, lastName, email, password, username, "https://plus.unsplash.com/premium_photo-1677094310899-02303289cadf?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
            return redirectWithAttributes("/login", redirectAttributes, model);
        } catch (Exception e) {
            return "register";
        }
    }

    @GetMapping("/home")
    public ModelAndView home() {
        Page<PostSummaryDTO> posts = postService.getPosts();

        // Truncate descriptions to a max length
        int maxLength = 120; // Set your desired max length
        posts.forEach(post -> {
            if (post.getDescription().length() > maxLength) {
                post.setDescription(post.getDescription().substring(0, maxLength) + "...");
            }
        });

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
    
    @GetMapping("/post")
    public String getSinglePost() {
        return "postView";
    }
}

