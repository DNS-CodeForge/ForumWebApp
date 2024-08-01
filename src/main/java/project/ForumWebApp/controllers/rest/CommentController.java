package project.ForumWebApp.controllers.rest;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.services.CommentService;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all comments", description = "Fetches a list of all comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of comments retrieved successfully")
    })
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific comment by ID", description = "Fetches the details of a specific comment based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    public CommentDTO getCommentById(@PathVariable int id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("!hasRole('BANNED')")
    @Operation(summary = "Create a new comment", description = "Creates a new comment associated with the specified post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public CommentDTO createComment(@PathVariable int postId, @RequestBody CommentCreateDTO commentCreateDTO) {
        return commentService.createComment(postId, commentCreateDTO);
    }

    @PostMapping("/{id}")
    @PreAuthorize("@commentServiceImpl.isOwner(#id)")
    @Operation(summary = "Update an existing comment", description = "Updates the details of an existing comment based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public CommentDTO updateComment(@PathVariable int id, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isOwner(#id)")
    @Operation(summary = "Delete a comment", description = "Deletes a specific comment based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public void deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
    }
}
