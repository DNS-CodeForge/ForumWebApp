package project.ForumWebApp.controllers;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.services.PostServiceImpl;

@RestController
@RequestMapping("/api/post")
@CrossOrigin("*") 
public class PostController{
    private final PostServiceImpl postService;

    @Autowired
    public PostController(PostServiceImpl postService) {
        this.postService = postService;
    }
     
    @GetMapping
    public List<PostDTO> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable int id) {
        return postService.getPost(id).get();
    }
}
