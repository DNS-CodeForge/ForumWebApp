package project.ForumWebApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;

public interface PostService {

    PostDTO createPost(PostCreateDTO postDTO);

    PostDTO updatePost(int id, PostUpdateDTO PostUpdateDTO);

    void deletePost(int id);

    PostDTO getPost(int id);

    List<PostSummaryDTO> getPosts(String title, String description, String user, List<String> tags, String sort, Pageable pageable);

    List<PostSummaryDTO> getPosts(String title, String description, List<String> tags, String sort, Pageable pageable);

    boolean isOwner(int id);
}
