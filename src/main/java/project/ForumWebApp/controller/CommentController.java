package project.ForumWebApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/post/{id}")
    public CommentDTO createComment(@PathVariable int postId, @RequestBody CommentDTO commentDTO) {
        commentDTO.setPostId(postId);
        return commentService.createComment(commentDTO);
    }

    @PostMapping("/{id}")
    public CommentDTO updateComment(@PathVariable int id, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
    }
}

