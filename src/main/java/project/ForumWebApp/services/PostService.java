package project.ForumWebApp.services;


import java.util.List;
import java.util.Optional;

import project.ForumWebApp.models.Post;



public interface PostService {
    Post createPost(Post post);
    Post updatePost(Post post);
    void deletePost(Post post);
    Optional<Post> getPost(int id);
    List<Post> getAllPosts();
    List<Post> findPostByTitleContaining(String title);
}

