package project.ForumWebApp.services;

import java.util.List;

import project.ForumWebApp.models.DTOs.CommentDTO;

public interface CommentService {
    List<CommentDTO> getAllComments();

    CommentDTO getCommentById(Integer id);

    CommentDTO createComment(CommentDTO commentDTO);

    CommentDTO updateComment(Integer id, CommentDTO commentDTO);

    void deleteComment(Integer id);

    List<CommentDTO> getCommentsByPostId(int postId);
}
