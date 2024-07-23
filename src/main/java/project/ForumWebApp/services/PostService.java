package project.ForumWebApp.services;

import project.ForumWebApp.models.DTOs.PostCreateDTO;
import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.models.DTOs.PostSummaryDTO;

import java.util.List;
import java.util.Optional;

public interface PostService {

    PostDTO createPost(PostCreateDTO postDTO);

    PostDTO updatePost(PostDTO postDTO);

    void deletePost(int id);

    Optional<PostDTO> getPost(int id);

    List<PostSummaryDTO> getPosts(String title, String description, String user, List<String> tags, String sort);
}
