package project.ForumWebApp.services.contracts;

import java.util.List;

import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.CommentDTO;

public interface CommentService {
    List<CommentDTO> getAllComments();

    CommentDTO getCommentById(Integer id);

    CommentDTO createComment(int id, CommentCreateDTO commentDTO);

    CommentDTO updateComment(Integer id, CommentDTO commentDTO);

    void deleteComment(Integer id);

    List<CommentDTO> getCommentsByPostId(int postId);

    boolean isOwner(int id);
}
