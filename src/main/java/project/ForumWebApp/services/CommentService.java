package project.ForumWebApp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.repository.CommentRepository;
import project.ForumWebApp.repository.PostRepository;

@Service
public class CommentService {
     
    private final CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
    }

    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                       .map(comment -> modelMapper.map(comment, CommentDTO.class))
                       .collect(Collectors.toList());
    }

    public CommentDTO getCommentById(Integer id) {
        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Comment not found"));
        return modelMapper.map(comment, CommentDTO.class);
    }

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(postRepository.findById(commentDTO.getPostId()).get());
        Comment savedComment = commentRepository.save(comment);
        return modelMapper.map(savedComment, CommentDTO.class);
    }

    public CommentDTO updateComment(Integer id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment.setContent(commentDTO.getContent());
        existingComment.setPost(postRepository.findById(commentDTO.getPostId()).get());
        Comment updatedComment = commentRepository.save(existingComment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

}
