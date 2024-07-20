package project.ForumWebApp.services;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.Comment;
import project.ForumWebApp.models.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.ForumWebApp.models.DTOs.PostDTO;
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
    public Optional<PostDTO> getPost(int id) {
        return postRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
     
    @Override
    public List<Post> findPostByTitleContaining(String title) {
        return postRepository.findByTitle(title);
    }

    private PostDTO convertToDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setDescription(post.getDescription());
        postDTO.setCreatedDate(post.getCreatedDate());
        postDTO.setUserId(post.getUser().getId());
        postDTO.setSubForumId(post.getSubForum().getId());
        postDTO.setCommentIds(post.getComments().stream().map(Comment::getId).collect(Collectors.toSet()));
        postDTO.setTagIds(post.getTags().stream().map(Tag::getId).collect(Collectors.toSet()));
        return postDTO;
    }
}
