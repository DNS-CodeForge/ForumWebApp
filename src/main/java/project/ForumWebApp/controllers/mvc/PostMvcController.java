package project.ForumWebApp.controllers.mvc;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.web.util.UriComponentsBuilder;
import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;

import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.services.contracts.CommentService;
import project.ForumWebApp.services.contracts.LikeService;
import project.ForumWebApp.services.contracts.PostService;

@Controller
public class PostMvcController {

    private final PostService postService;
    private final CommentService commentService;

    private final AuthContextManager authContextManager;
    private final LikeService likeService;
    private final ModelMapper modelMapper;

    public PostMvcController(PostService postService, CommentService commentService, ModelMapper modelMapper, AuthContextManager authContextManager, LikeService likeService) {
        this.postService = postService;
        this.commentService = commentService;

        this.authContextManager = authContextManager;
        this.likeService = likeService;
        this.modelMapper = modelMapper;
    }
    @GetMapping("/search")
    public String loadSearchFormAndSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request,
            Model model
    ) {

        if (title == null && description == null && (tags == null || tags.isEmpty()) && username==null && sort == null) {
            return "advanced-search";
        }


        Pageable pageable = PageRequest.of(page, size);
        Page<PostSummaryDTO> posts = postService.getPosts(title, description, username, tags, sort, pageable);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("page", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("pageSize", size);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .query(request.getQueryString());

        if (posts.hasNext()) {
            String nextPageUrl = uriBuilder.replaceQueryParam("page", page + 1).toUriString();
            model.addAttribute("nextPage", nextPageUrl);
        }
        if (posts.hasPrevious()) {
            String previousPageUrl = uriBuilder.replaceQueryParam("page", page - 1).toUriString();
            model.addAttribute("previousPage", previousPageUrl);
        }

        return "advanced-search-results";
    }
    @ModelAttribute("loggedInUser")
    public ApplicationUser addUserToModel() {
        return authContextManager.getLoggedInUser();
    }


    @GetMapping("/post")
    public String getPostCreatePage(Model model) {
        model.addAttribute("post", new PostCreateDTO());
        return "create-post";
    }

    @PostMapping("/post")
    public String createPost(@Valid @ModelAttribute("post") PostCreateDTO postCreateDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "create-post";
        }


        postService.createPost(postCreateDTO);
        redirectAttributes.addFlashAttribute("message", "Post created successfully!");
        return "redirect:/home";
    }
    @PreAuthorize("@postServiceImpl.isOwner(#postId)")
    @GetMapping("/post/edit/{id}")
    public String showEditPostPage(@PathVariable("id") int postId, Model model) {
        PostDTO post = postService.getPost(postId);
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PreAuthorize("@postServiceImpl.isOwner(#postId)")
    @PostMapping("/post/edit/{id}")
    public String updatePost(@PathVariable("id") int postId,
                             @Valid @ModelAttribute("post") PostUpdateDTO post,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        if (bindingResult.hasErrors()) {
            var postEr = modelMapper.map(post, PostUpdateDTO.class);

            model.addAttribute("post", postEr);
            return "edit-post";
        }

        try {
            if (post.getTags() == null) {
                post.setTags(new ArrayList<>());
            }

            postService.updatePost(postId, post);

            redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update the post. Please try again.");
        }
        return "redirect:/posts/" + postId;
    }

    @GetMapping("/posts/{id}")
    public String getPostDetail(@PathVariable Integer id, Model model) {
        PostDTO post = postService.getPost(id);
        try {
            boolean isOwner;
            try {
                 isOwner = postService.isOwner(id);
            } catch (AuthorizationException e) {
                 isOwner = false;
            }
            model.addAttribute("post", post);
            model.addAttribute("comments", commentService.getCommentsByPostId(id));
            model.addAttribute("isOwner", isOwner);
            model.addAttribute("author", post.getUser().getUsername());
            return "post-detail";

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/home";
        }
    }


    @PostMapping("/posts/{id}/comments")
    public String addComment(@PathVariable Integer id, HttpServletRequest request) {
        String content = request.getParameter("content");
        commentService.createComment(id, new CommentCreateDTO(content));

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
            @RequestParam(required = false) String username,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<PostSummaryDTO> posts;
        if (username != null) {
            posts = postService.getPosts(title, description, username, tags, sort, pageable);
        } else {
            posts = postService.getPosts(title, description, null, tags, sort, pageable);
        }

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("hasNextPage", posts.hasNext());

        return "/fragments/posts :: postList";
    }

    @GetMapping("/profile/info/liked/loadMore")
    public String loadMoreLikedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        PageRequest pageable = PageRequest.of(page, size);

        Page<PostSummaryDTO> posts = likeService.getAllPostsLikedByUser(userProfile, pageable);

        model.addAttribute("posts", posts.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", posts.getTotalPages());
        model.addAttribute("hasNextPage", posts.hasNext());

        return "/fragments/posts :: postList";  // Reuse the same fragment for rendering posts
    }

    @PostMapping("/like/post/{id}")
    public ResponseEntity<String> likePost(@PathVariable int id) {
        try {
            likeService.likePost(id);
            return ResponseEntity.ok("Liked/Unliked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @GetMapping("/profile/info/comments/loadMore")
    public String loadMoreUserComments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model
    ) {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        PageRequest pageable = PageRequest.of(page, size);

        Page<CommentDTO> comments = commentService.getCommentsByUser(userProfile, pageable);

        model.addAttribute("comments", comments.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", comments.getTotalPages());
        model.addAttribute("hasNextPage", comments.hasNext());

        return "/fragments/comments :: commentList";
    }

    @DeleteMapping("/posts/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Integer id) {
        try {
            postService.deletePost(id);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete the post", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
