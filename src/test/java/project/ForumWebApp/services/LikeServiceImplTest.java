package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Like;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.repository.LikeRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.Implementations.LikeServiceImpl;
import project.ForumWebApp.services.contracts.LevelService;

@ExtendWith(MockitoExtension.class)
class LikeServiceImplTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private AuthContextManager authContextManager;

    @Mock
    private LevelService levelService;

    @InjectMocks
    private LikeServiceImpl likeService;

    private ApplicationUser user;
    private Post post;
    private Like like;

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        user.setId(1);
        user.setUsername("testuser");

        post = new Post();
        post.setId(1);
        post.setTitle("Test Post");
        post.setDescription("Test Description");

        like = new Like();
        like.setUser(user);
        like.setPost(post);
    }

    @Test
    void likePost_UserNotLikedYet() {
        when(authContextManager.getLoggedInUser()).thenReturn(user);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeRepository.findLikeByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.empty());

        likeService.likePost(1);

        verify(postRepository, times(1)).save(post);
        verify(likeRepository, times(1)).save(any(Like.class));
        verify(levelService, times(1)).addExp(post.getUser(), 1);
    }

    @Test
    void likePost_UserAlreadyLiked() {
        when(authContextManager.getLoggedInUser()).thenReturn(user);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeRepository.findLikeByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.of(like));

        likeService.likePost(1);

        verify(postRepository, times(1)).save(post);
        verify(likeRepository, times(1)).delete(like);
        verify(levelService, times(1)).addExp(post.getUser(), -1);
    }

    @Test
    void likePost_PostDoesNotExist() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> likeService.likePost(1));
    }

    @Test
    void likePost_UserNotLoggedIn() {
        when(authContextManager.getLoggedInUser()).thenThrow(new NoSuchElementException("User not logged in"));

        assertThrows(NoSuchElementException.class, () -> likeService.likePost(1));
    }

    @Test
    void likePost_LikeRepositoryThrowsException() {
        when(authContextManager.getLoggedInUser()).thenReturn(user);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeRepository.findLikeByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(likeRepository).save(any(Like.class));

        assertThrows(RuntimeException.class, () -> likeService.likePost(1));
    }

    @Test
    void likePost_PostRepositoryThrowsException() {
        when(authContextManager.getLoggedInUser()).thenReturn(user);
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(likeRepository.findLikeByUserIdAndPostId(user.getId(), post.getId())).thenReturn(Optional.empty());
        doThrow(new RuntimeException("Database error")).when(postRepository).save(post);

        assertThrows(RuntimeException.class, () -> likeService.likePost(1));
    }
}
