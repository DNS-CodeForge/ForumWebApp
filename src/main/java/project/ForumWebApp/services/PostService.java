package project.ForumWebApp.services;

import java.util.List;
import java.util.Optional;

import project.ForumWebApp.models.DTOs.PostCreateDTO;
import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.models.DTOs.PostSummaryDTO;

public interface PostService {
    PostDTO createPost(PostCreateDTO postDTO);
    PostDTO updatePost(PostDTO postDTO);
    void deletePost(int id);
    Optional<PostDTO> getPost(int id);
    List<PostSummaryDTO> getAllPosts();
    List<PostSummaryDTO> findPostByTitleContaining(String title);
}
