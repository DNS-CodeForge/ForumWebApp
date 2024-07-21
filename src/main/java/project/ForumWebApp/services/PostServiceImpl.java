package project.ForumWebApp.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.ForumWebApp.models.Post;
import project.ForumWebApp.repository.PostRepository;

@Service
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }   

    @Override
    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Optional<Post> getPost(int id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
     
    @Override
    public List<Post> findPostByTitleContaining(String title) {
        return postRepository.findByTitle(title);
    }
}
