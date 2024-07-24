package project.ForumWebApp.services;

import project.ForumWebApp.models.DTOs.CommentDTO;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllComments();

    CommentDTO getCommentById(Integer id);

    CommentDTO createComment(CommentDTO commentDTO);

    CommentDTO updateComment(Integer id, CommentDTO commentDTO);

    void deleteComment(Integer id);
}
