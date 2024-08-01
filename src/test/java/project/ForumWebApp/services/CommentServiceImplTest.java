package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.DTOs.CommentCreateDTO;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.repository.CommentRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.Implementations.CommentServiceImpl;
import project.ForumWebApp.services.contracts.LevelService;

import static project.ForumWebApp.constants.ValidationConstants.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthContextManager authContextManager;

    @Mock
    private LevelService levelService;

    @InjectMocks
    private CommentServiceImpl commentService;

    private ApplicationUser user;
    private Post post;
    private Comment comment;
    private CommentDTO commentDTO;
    private CommentCreateDTO commentCreateDTO;

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        user.setId(1);
        user.setUsername("testuser");

        post = new Post();
        post.setId(1);
        post.setTitle("Test Post");
        post.setDescription("Test Description");

        comment = new Comment();
        comment.setId(1);
        comment.setContent("Test Comment");
        comment.setPost(post);
        comment.setUser(user);

        commentDTO = new CommentDTO();
        commentDTO.setId(1);
        commentDTO.setContent("Updated Test Comment");

        commentCreateDTO = new CommentCreateDTO();
        commentCreateDTO.setContent("Test Comment Create DTO");
    }

    @Test
    void getCommentsByPostId() {
        when(commentRepository.findByPostId(1)).thenReturn(Collections.singletonList(comment));
        when(modelMapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        List<CommentDTO> result = commentService.getCommentsByPostId(1);

        assertEquals(1, result.size());
        assertEquals(commentDTO, result.get(0));
    }

    @Test
    void getAllComments() {
        when(commentRepository.findAll()).thenReturn(Collections.singletonList(comment));
        when(modelMapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        List<CommentDTO> result = commentService.getAllComments();

        assertEquals(1, result.size());
        assertEquals(commentDTO, result.get(0));
    }

    @Test
    void getCommentById() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(modelMapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.getCommentById(1);

        assertEquals(commentDTO, result);
    }

    @Test
    void getCommentById_NotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(1));

        assertEquals(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void createComment() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getLoggedInUser()).thenReturn(user);
        when(modelMapper.map(any(CommentCreateDTO.class), eq(Comment.class))).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(modelMapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.createComment(1, commentCreateDTO);

        assertEquals(commentDTO, result);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void createComment_PostNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> commentService.createComment(1, commentCreateDTO));

        assertEquals(POST_WITH_PROVIDED_ID_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void updateComment() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(authContextManager.getUsername()).thenReturn(user.getUsername());
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(modelMapper.map(any(Comment.class), eq(CommentDTO.class))).thenReturn(commentDTO);

        CommentDTO result = commentService.updateComment(1, commentDTO);

        assertEquals(commentDTO, result);
    }

    @Test
    void updateComment_NotAuthorized() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(authContextManager.getUsername()).thenReturn("differentUser");

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> commentService.updateComment(1, commentDTO));

        assertEquals(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT, exception.getMessage());
    }

    @Test
    void updateComment_NotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(1, commentDTO));

        assertEquals(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void deleteComment() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(authContextManager.getUsername()).thenReturn(user.getUsername());

        commentService.deleteComment(1);

        verify(commentRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteComment_NotAuthorized() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(authContextManager.getUsername()).thenReturn("differentUser");

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> commentService.deleteComment(1));

        assertEquals(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT, exception.getMessage());
    }

    @Test
    void deleteComment_NotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> commentService.deleteComment(1));

        assertEquals(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST, exception.getMessage());
    }

    @Test
    void isOwner() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(authContextManager.getUsername()).thenReturn(user.getUsername());

        assertTrue(commentService.isOwner(1));
    }

    @Test
    void isOwner_NotAuthorized() {
        when(commentRepository.findById(1)).thenReturn(Optional.of(comment));
        when(authContextManager.getUsername()).thenReturn("differentUser");

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> commentService.isOwner(1));

        assertEquals(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_COMMENT, exception.getMessage());
    }

    @Test
    void isOwner_CommentNotFound() {
        when(commentRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> commentService.isOwner(1));

        assertEquals(COMMENT_WITH_PROVIDED_ID_DOES_NOT_EXIST, exception.getMessage());
    }
}
