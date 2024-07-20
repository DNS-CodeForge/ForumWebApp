package project.ForumWebApp.services;


import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.models.Post;

import java.util.List;
import java.util.Optional;



public interface PostService {
    Post createPost(Post post);
    Post updatePost(Post post);
    void deletePost(Post post);
    Optional<PostDTO> getPost(int id);
    List<PostDTO> getAllPosts();
    List<Post> findPostByTitleContaining(String title);
}

