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
import org.springframework.web.bind.annotation.RestController;

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
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("/{id}")
    public CommentDTO getCommentById(@PathVariable int id) {
        return commentService.getCommentById(id);
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("!hasRole('BANNED')")
    public CommentDTO createComment(@PathVariable int postId, @RequestBody CommentCreateDTO commentDTO) {
        return commentService.createComment(postId, commentDTO);
    }

    @PostMapping("/{id}")
    @PreAuthorize("@commentServiceImpl.isOwner(#id)")
    public CommentDTO updateComment(@PathVariable int id, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @commentServiceImpl.isOwner(#id)")
    public void deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
    }
}

