package project.ForumWebApp.services.Implementations;

import static project.ForumWebApp.constants.ValidationConstants.COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST;
import static project.ForumWebApp.constants.ValidationConstants.POST_WITH_PROVIDED_ID_DOES_NOT_EXIST;
import static project.ForumWebApp.constants.ValidationConstants.YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.repository.CommentRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.contracts.CommentService;
import project.ForumWebApp.services.contracts.LevelService;

@Service
public class CommentServiceImpl implements CommentService {




    private final LevelService levelService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final AuthContextManager authContextManager;

    public CommentServiceImpl(LevelService levelService, CommentRepository commentRepository, ModelMapper modelMapper,
                              PostRepository postRepository, AuthContextManager authContextManager) {

        this.levelService = levelService;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.postRepository = postRepository;
        this.authContextManager = authContextManager;
    }

    @Override
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
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST));
        return modelMapper.map(comment, CommentDTO.class);
    }

    @Override
    @Transactional
    public CommentDTO createComment(int id, CommentCreateDTO commentDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POST_WITH_PROVIDED_ID_DOES_NOT_EXIST));
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        System.out.println(comment.getContent());
        System.out.println(commentDTO.getContent());
        comment.setPost(post);
        comment.setUser(authContextManager.getLoggedInUser());
        Comment savedComment = commentRepository.save(comment);
        post.getComments().add(savedComment);
        levelService.addExp(post.getUser(), 5);
        postRepository.save(post);
        return modelMapper.map(savedComment, CommentDTO.class);
    }

    @Override
    @Transactional
    public CommentDTO updateComment(Integer id, CommentDTO commentDTO) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST));

        if (!existingComment.getUser().getUsername().equals(authContextManager.getUsername())) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT);
        }

        existingComment.setContent(commentDTO.getContent());
        Comment updatedComment = commentRepository.save(existingComment);
        return modelMapper.map(updatedComment, CommentDTO.class);
    }

    @Override
    public void deleteComment(Integer id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST));

        if (!comment.getUser().getUsername().equals(authContextManager.getUsername())) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT);
        }

        commentRepository.deleteById(id);
    }

    @Override
    public boolean isOwner(int id) {
        String currentUsername = authContextManager.getUsername();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST));
        if (!comment.getUser().getUsername().equals(currentUsername)) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT);
        }
        return true;
    }
}
