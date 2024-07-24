package project.ForumWebApp.services;

import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDTO createPost(PostCreateDTO postDTO);

    PostDTO updatePost(int id, PostUpdateDTO PostUpdateDTO);

    void deletePost(int id);

    Optional<PostDTO> getPost(int id);

    List<PostSummaryDTO> getPosts(String title, String description, String user, List<String> tags, String sort);
}
