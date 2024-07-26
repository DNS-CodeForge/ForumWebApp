package project.ForumWebApp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.services.PostService;

@RestController
@RequestMapping("/api/post")
@CrossOrigin("*")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostSummaryDTO> getAllPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sort) {

        return postService.getPosts(title, description, tags, sort);
    }


    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable int id) {
        return postService.getPost(id).get();
    }

    @PostMapping
    @PreAuthorize("!hasRole('BANNED')")
    public PostDTO createPost(@RequestBody PostCreateDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @PostMapping("/{id}")
    @PreAuthorize("@postServiceImpl.isOwner(#id)")
    public PostDTO updatePost(@PathVariable int id, @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(id, postUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@postServiceImpl.isOwner(#id) or hasRole('ADMIN')")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }

}
