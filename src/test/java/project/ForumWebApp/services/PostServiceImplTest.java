package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.data.jpa.domain.Specification;
import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.exceptions.AuthorizationException;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.repository.CommentRepository;
import project.ForumWebApp.repository.LikeRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.Implementations.PostServiceImpl;
import project.ForumWebApp.services.TagService;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TagService tagService;

    @Mock
    private AuthContextManager authContextManager;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private PostServiceImpl postService;

    private Post post;
    private Post anotherPost;
    private Tag tag;
    private PostCreateDTO postCreateDTO;
    private PostUpdateDTO postUpdateDTO;
    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        user = new ApplicationUser();
        user.setUsername("testuser");

        post = new Post();
        post.setId(1);
        post.setTitle("Test Post");
        post.setDescription("Test Description");
        post.setTags(new HashSet<>());
        post.setUser(user);

        anotherPost = new Post();
        anotherPost.setId(2);
        anotherPost.setTitle("Another Post");
        anotherPost.setDescription("Another Description");
        anotherPost.setTags(new HashSet<>());
        anotherPost.setUser(user);

        tag = new Tag(1, "Tag1", new HashSet<>());
        postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("Test Post");
        postCreateDTO.setDescription("Test Description");
        postCreateDTO.setTagNames(Set.of("Tag1"));

        postUpdateDTO = new PostUpdateDTO();
        postUpdateDTO.setTitle("Updated Title");
        postUpdateDTO.setDescription("Updated Description");
        postUpdateDTO.setTags(List.of("Tag1"));
    }


    @Test
    void createPost_PostExists() {
        when(postRepository.findByTitle(postCreateDTO.getTitle())).thenReturn(Optional.of(post));

        EntityExistsException thrown = assertThrows(EntityExistsException.class, () -> {
            postService.createPost(postCreateDTO);
        });

        assertEquals("Post with this title already exists.", thrown.getMessage());
    }

    @Test
    void createPost_PostDoesNotExist() {
        when(postRepository.findByTitle(postCreateDTO.getTitle())).thenReturn(Optional.empty());
        when(authContextManager.getLoggedInUser()).thenReturn(user);
        when(tagService.findTagByName("Tag1")).thenReturn(Optional.of(tag));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(modelMapper.map(postCreateDTO, Post.class)).thenReturn(post);
        when(modelMapper.map(post, PostDTO.class)).thenReturn(new PostDTO());

        PostDTO result = postService.createPost(postCreateDTO);

        assertNotNull(result);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void updatePost_PostNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            postService.updatePost(1, postUpdateDTO);
        });

        assertEquals("Post with provided ID does not exist", thrown.getMessage());
    }

    @Test
    void updatePost_NotAuthorized() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getUsername()).thenReturn("anotherUser");

        AuthorizationException thrown = assertThrows(AuthorizationException.class, () -> {
            postService.updatePost(1, postUpdateDTO);
        });

        assertEquals("You are not authorized to update this post", thrown.getMessage());
    }

    @Test
    void updatePost_Success() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getUsername()).thenReturn("testuser");
        when(tagService.findTagByName("Tag1")).thenReturn(Optional.of(tag));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(modelMapper.map(post, PostDTO.class)).thenReturn(new PostDTO());

        PostDTO result = postService.updatePost(1, postUpdateDTO);

        assertNotNull(result);
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void deletePost_PostNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            postService.deletePost(1);
        });

        assertEquals("Post with provided ID does not exist", thrown.getMessage());
    }

    @Test
    void deletePost_NotAuthorized() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getUsername()).thenReturn("anotherUser");

        AuthorizationException thrown = assertThrows(AuthorizationException.class, () -> {
            postService.deletePost(1);
        });

        assertEquals("You are not authorized to update this post", thrown.getMessage());
    }

    @Test
    void deletePost_Success() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getUsername()).thenReturn("testuser");

        postService.deletePost(1);

        verify(postRepository).deleteById(1);
        verify(commentRepository).deleteByPostId(1);
        verify(likeRepository).deleteByPostId(1);
    }

    @Test
    void getPost_PostNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            postService.getPost(1);
        });

        assertEquals("Post with provided ID does not exist", thrown.getMessage());
    }

    @Test
    void getPost_Success() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(modelMapper.map(post, PostDTO.class)).thenReturn(new PostDTO());

        PostDTO result = postService.getPost(1);

        assertNotNull(result);
    }

    @Test
    void getPosts_WithFilters() {
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts("title", "description", "user", List.of("tag"), "sort");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getPosts_WithoutUser() {
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts("title", "description", List.of("tag"), "sort");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void isOwner_NotOwner() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getUsername()).thenReturn("anotherUser");

        AuthorizationException thrown = assertThrows(AuthorizationException.class, () -> {
            postService.isOwner(1);
        });

        assertEquals("You are not authorized to update this post", thrown.getMessage());
    }

    @Test
    void isOwner_Owner() {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(authContextManager.getUsername()).thenReturn("testuser");

        boolean result = postService.isOwner(1);

        assertTrue(result);
    }
    @Test
    void getPosts_NoFilters() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPosts_WithTitleFilter() {
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts("Test", null, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPosts_WithDescriptionFilter() {
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, "Description", null, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPosts_WithUserFilter() {
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, "testuser", null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPosts_WithTagsFilter() {
        post.setTags(Set.of(tag));
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, List.of("Tag1"), null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPosts_WithMultipleFilters() {
        post.setTags(Set.of(tag));
        List<Post> posts = List.of(post);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts("Test", "Description", "testuser", List.of("Tag1"), null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getPosts_SortByCommentsAsc() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, "commentsAsc");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPosts_SortByCommentsDesc() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, "commentsDesc");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPosts_SortByDateAsc() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, "dateAsc");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPosts_SortByDateDesc() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, "dateDesc");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPosts_SortByLikesAsc() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, "likesAsc");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getPosts_SortByLikesDesc() {
        List<Post> posts = List.of(post, anotherPost);
        when(postRepository.findAll(any(Specification.class))).thenReturn(posts);
        when(modelMapper.map(post, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());
        when(modelMapper.map(anotherPost, PostSummaryDTO.class)).thenReturn(new PostSummaryDTO());

        List<PostSummaryDTO> result = postService.getPosts(null, null, null, null, "likesDesc");

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
