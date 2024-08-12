package project.ForumWebApp.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.services.contracts.CommentService;
import project.ForumWebApp.services.contracts.PostService;
import project.ForumWebApp.services.contracts.UserService;

@Controller
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final AuthContextManager authContextManager;

    public PostMvcController(PostService postService, CommentService commentService, ModelMapper modelMapper, AuthContextManager authContextManager) {
        this.postService = postService;
        this.commentService = commentService;
        this.modelMapper = modelMapper;
        this.authContextManager = authContextManager;
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
}

