package project.ForumWebApp.services.contracts;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.models.Post;

public interface PostService {

    PostDTO createPost(PostCreateDTO postDTO);

    PostDTO updatePost(int id, PostUpdateDTO PostUpdateDTO);

    void deletePost(int id);

    PostDTO getPost(int id);

    Page<PostSummaryDTO> getPosts(String title, String description, String user, List<String> tags, String sort, Pageable pageable);

    Page<PostSummaryDTO> getPosts(String title, String description, List<String> tags, String sort, Pageable pageable);
    Page<PostSummaryDTO> getPosts();


    boolean isOwner(int id);
}
