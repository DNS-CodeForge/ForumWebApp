package project.ForumWebApp.services.Implementations;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.repository.CommentRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
     
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final AuthContextManager authContextManager;

    public CommentServiceImpl(CommentRepository commentRepository, ModelMapper modelMapper,
                              PostRepository postRepository,AuthContextManager authContextManager) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.authContextManager = authContextManager;
    }

    public List<CommentDTO> getCommentsByPostId(int postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                       .map(comment -> modelMapper.map(comment, CommentDTO.class))
                       .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                       .map(comment -> modelMapper.map(comment, CommentDTO.class))
                       .collect(Collectors.toList());
    }

    @Override
    public CommentDTO getCommentById(Integer id) {
        Comment comment = commentRepository.findById(id)
                                           .orElseThrow(() -> new RuntimeException("Comment not found"));
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    @Transactional
    public CommentDTO createComment(int id, CommentCreateDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        Post post  = postRepository.findById(id).get();
        comment.setPost(post);
        comment.setUser(authContextManager.getLoggedInUser());
        Comment savedComment = commentRepository.save(comment);
        post.getComments().add(savedComment);
        postRepository.save(post);
        return modelMapper.map(savedComment, CommentDTO.class);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Integer id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                                                   .orElseThrow(() -> new RuntimeException("Comment not found"));
        existingComment.setContent(commentDTO.getContent());
        Comment updatedComment = commentRepository.save(existingComment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    @Override
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }


    @Override
    public boolean isOwner(int id) {
        String currentUsername = authContextManager.getUsername();
        Comment comment = commentRepository.findById(id).orElse(null);
        return comment != null && comment.getUser().getUsername().equals(currentUsername);
    }
}
