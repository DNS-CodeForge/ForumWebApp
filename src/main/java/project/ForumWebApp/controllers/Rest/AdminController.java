package project.ForumWebApp.controllers.Rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.services.contracts.PostService;
import project.ForumWebApp.services.contracts.UserService;

@RestController
@RequestMapping("api/admin")
//@CrossOrigin
public class AdminController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public AdminController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @Operation(summary = "Admin level access test endpoint")
    @GetMapping
    public String helloAdminController() {
        return "Admin level access";
    }

    @Operation(summary = "Get all posts with optional filters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PostSummaryDTO.class))))
    })
    @GetMapping("/post")
    public Page<PostSummaryDTO> getAllPosts(
            @Parameter(description = "Filter by title") @RequestParam(required = false) String title,
            @Parameter(description = "Filter by description") @RequestParam(required = false) String description,
            @Parameter(description = "Filter by user") @RequestParam(required = false) String user,
            @Parameter(description = "Filter by tags") @RequestParam(required = false) List<String> tags,
            @Parameter(description = "Sort by field") @RequestParam(required = false) String sort,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return postService.getPosts(title, description, user, tags, sort, pageable);
    }

    @Operation(summary = "Get a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the post", content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @GetMapping("/post/{id}")
    public PostDTO getPostById(@PathVariable int id) {
        return postService.getPost(id);
    }

    @PostMapping("/user/{id}/roles")
    public ApplicationUser setUserRole(@PathVariable("id") int userId,
                                       @RequestParam(required = false, name = "addRole") String roleToAdd,
                                       @RequestParam(required = false, name = "removeRole") String roleToRemove) {

        return userService.setUserRole(userId, roleToAdd, roleToRemove);
    }

    @PostMapping("/user/{id}/ban")
    public ApplicationUser banUserRole(@PathVariable("id") int userId) {

        return userService.setUserRole(userId, "BANNED", null);
    }

    @PostMapping("/user/{id}/unban")
    public ApplicationUser unbanUserRole(@PathVariable("id") int userId) {

        return userService.setUserRole(userId, null, "BANNED");
    }


    @Operation(summary = "Create a new post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully", content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/post")
    public PostDTO createPost(@RequestBody PostCreateDTO postDTO) {
        return postService.createPost(postDTO);
    }

    @Operation(summary = "Update an existing post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated successfully", content = @Content(schema = @Schema(implementation = PostDTO.class))),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @PostMapping("/post/{id}")
    public PostDTO updatePost(@PathVariable int id, @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.updatePost(id, postUpdateDTO);
    }

    @Operation(summary = "Delete a post by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content)
    })
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }
}
