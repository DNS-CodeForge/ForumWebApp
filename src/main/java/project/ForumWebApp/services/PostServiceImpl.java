package project.ForumWebApp.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.ForumWebApp.models.DTOs.PostSummaryDTO;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.repository.UserRepository;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(userRepository.findByUsername(postDTO.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))); // Adjust error handling as needed
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    @Transactional
    public PostDTO updatePost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(userRepository.findByUsername(postDTO.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"))); // Adjust error handling as needed
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    @Transactional
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    @Override

    public Optional<PostDTO> getPost(int id) {
        return postRepository.findById(id)
                .map(post -> modelMapper.map(post, PostDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> findPostByTitleContaining(String title) {
        return postRepository.findByTitleContaining(title).stream()
                .map(post -> modelMapper.map(post, PostSummaryDTO.class))
                .collect(Collectors.toList());
    }
}
