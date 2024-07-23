package project.ForumWebApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import project.ForumWebApp.models.DTOs.PostCreateDTO;
import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.models.DTOs.PostSummaryDTO;
import project.ForumWebApp.services.PostServiceImpl;

@RestController
@RequestMapping("/api/post")
@CrossOrigin("*")
public class PostController {
    private final PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostSummaryDTO> getAllPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String user,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sort) {

        return postService.getPosts(title, description, user, tags, sort);
    }


    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable int id) {
        return postService.getPost(id).get();
    }

    @PostMapping
    public PostDTO createPost(@RequestBody PostCreateDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }
}
