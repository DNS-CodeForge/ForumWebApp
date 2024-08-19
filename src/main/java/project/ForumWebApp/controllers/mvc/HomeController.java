package project.ForumWebApp.controllers.mvc;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.services.contracts.PostService;

@Controller
public class HomeController {

    private final PostService postService;
    private final AuthContextManager authContextManager;

    public HomeController(PostService postService, AuthContextManager authContextManager) {
        this.postService = postService;
        this.authContextManager = authContextManager;
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView home() {
        var user = authContextManager.getLoggedInUser();
        System.out.println(user.getPhotoUrl());
        Page<PostSummaryDTO> posts = postService.getPosts();


        int maxLength = 120;
        posts.forEach(post -> {
            if (post.getDescription().length() > maxLength) {
                post.setDescription(post.getDescription().substring(0, maxLength) + "...");
            }
        });

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("posts", posts);
        return modelAndView;
    }
    @ModelAttribute("loggedInUser")
    public ApplicationUser addUserToModel() {
        return authContextManager.getLoggedInUser();
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }

   @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "admin-panel";
    }


}
