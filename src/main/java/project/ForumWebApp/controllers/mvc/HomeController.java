package project.ForumWebApp.controllers.mvc;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.services.contracts.PostService;

@Controller
public class HomeController {

    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public ModelAndView home() {
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
