package project.ForumWebApp.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.services.contracts.CommentService;
import project.ForumWebApp.services.contracts.LikeService;
import project.ForumWebApp.services.contracts.PostService;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final AuthContextManager authContextManager;
    private final LikeService likeService;

    public PostMvcController(PostService postService, CommentService commentService, ModelMapper modelMapper, AuthContextManager authContextManager, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.authContextManager = authContextManager;
        this.likeService = likeService;
    }

    @GetMapping("/post")
    public String getPostCreatePage(Model model) {
        model.addAttribute("post", new PostCreateDTO());
        return "createPost";
    }

    @PostMapping("/post")
    public String createPost(@Valid @ModelAttribute("post") PostCreateDTO postCreateDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {

            return "createPost";
        }

        postService.createPost(postCreateDTO);
        redirectAttributes.addFlashAttribute("message", "Post created successfully!");
        return "redirect:/home";
    }
    @GetMapping("/posts/{id}")
    public String getPostDetail(@PathVariable Integer id, Model model) {
        PostDTO post= postService.getPost(id);
        try {
            model.addAttribute("post", post);
            model.addAttribute("comments", commentService.getCommentsByPostId(id));
            return "postDetail";

        } catch (Exception e) {
            return "redirect:/home";
        }
    }


    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable Integer id, HttpServletRequest request) {
        String content = request.getParameter("content");
        var comment = new Comment(0,content, authContextManager.getLoggedInUser(), modelMapper.map(postService.getPost(id), Post.class));

        postService.commentPost(id, comment);
        return "redirect:/posts/" + id;
    }
    @GetMapping("/posts/loadMore")
    public String loadMorePosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostSummaryDTO> posts = postService.getPosts(title, description, null, tags, sort, pageable);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("hasNextPage", posts.hasNext());

        return "/fragments/posts :: postList";
    }
}


