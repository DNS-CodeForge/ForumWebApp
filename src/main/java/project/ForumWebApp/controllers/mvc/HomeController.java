package project.ForumWebApp.controllers.mvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.services.PostService;

@Controller
@RequestMapping("/home")
public class HomeController {

    private PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
     public String home(Model model, Pageable pageable) {
        Page<PostSummaryDTO> posts = postService.getPosts(null, null, null, null, pageable);
        model.addAttribute("posts", posts.getContent());
        return "Home";
    }
}
