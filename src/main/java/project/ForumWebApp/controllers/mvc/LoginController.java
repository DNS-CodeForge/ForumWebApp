package project.ForumWebApp.controllers.mvc;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import project.ForumWebApp.controllers.Rest.AdminController;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.services.contracts.PostService;

import java.io.IOException;
import java.util.List;

@Controller
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final PostService postService;

    public LoginController(@Qualifier("mvcAuthenticationManager") AuthenticationManager authenticationManager,
                           AuthenticationFailureHandler authenticationFailureHandler, PostService postService) {
        this.authenticationManager = authenticationManager;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.postService = postService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @ModelAttribute("URL")
    public String getURL(final HttpServletRequest request) {

        return request.getRequestURI();
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
    @GetMapping("/home")
    public ModelAndView home() {


        Page<PostSummaryDTO> posts = postService.getPosts();
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
}
