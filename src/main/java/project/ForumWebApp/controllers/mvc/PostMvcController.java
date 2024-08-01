package project.ForumWebApp.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import project.ForumWebApp.services.PostService;

@Controller
@RequestMapping("/posts")
public class PostMvcController {
    private final PostService postService;

    public PostMvcController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public String getPost(@PathVariable int id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "Post";
    }
}
