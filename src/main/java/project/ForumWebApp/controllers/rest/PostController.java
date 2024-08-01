package project.ForumWebApp.controllers.rest;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import project.ForumWebApp.config.CustomResponse;
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

    @Operation(summary = "Get all posts with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostSummaryDTO.class))))
    })
    @GetMapping
    public ResponseEntity<CustomResponse> getAllPosts(
            @Parameter(description = "Filter by title") @RequestParam(required = false) String title,
            @Parameter(description = "Filter by description") @RequestParam(required = false) String description,
            @Parameter(description = "Filter by tags") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "Sort by field") @RequestParam(required = false) String sort,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostSummaryDTO> posts = postService.getPosts(title, description, null, tags, sort, pageable);
        var response = new CustomResponse();
        response.getData().put("page", page);
        response.getData().put("totalPages", posts.getTotalPages());
        response.getData().put("pageSize", size);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .query(request.getQueryString());

        if (posts.hasNext()) {
            String nextPageUrl = uriBuilder.replaceQueryParam("page", page + 1).toUriString();
            response.getData().put("nextPage", nextPageUrl);
        }
        if (posts.hasPrevious()) {
            String previousPageUrl = uriBuilder.replaceQueryParam("page", page - 1).toUriString();
            response.getData().put("previousPage", previousPageUrl);
        }
        response.getData().put("posts", posts.getContent());


        return ResponseEntity.ok(response);


    }

    @Operation(summary = "Get a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the post", content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @GetMapping("/{id}")
    public PostDTO getPostById(@PathVariable int id) {
        return postService.getPost(id);
    }

    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    @PreAuthorize("!hasRole('BANNED')")
    public PostDTO createPost(@RequestBody PostCreateDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @Operation(summary = "Update an existing post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @PostMapping("/{id}")
    @PreAuthorize("@postServiceImpl.isOwner(#id)")
    public PostDTO updatePost(@PathVariable int id, @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(id, postUpdateDTO);
    }

    @Operation(summary = "Delete a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("@postServiceImpl.isOwner(#id) or hasRole('ADMIN')")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }
}
