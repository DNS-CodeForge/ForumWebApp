package project.ForumWebApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.services.PostService;

import java.util.List;

@RestController
@RequestMapping("/admin")
//@CrossOrigin
public class AdminController {
    private final PostService postService;

    @Autowired
    public AdminController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public String helloAdminController() {
        return "Admin level access";
    }

    @GetMapping("/post")
    public List<PostSummaryDTO> getAllPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sort) {

        return postService.getPosts(title, description, user, tags, sort);

    }

    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable int id) {
        return postService.getPost(id).get();
    }

    @PostMapping("/post")
    public PostDTO createPost(@RequestBody PostCreateDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PostMapping("/post/{id}")
    public PostDTO updatePost(@PathVariable int id, @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(id, postUpdateDTO);
    }

    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }


}
