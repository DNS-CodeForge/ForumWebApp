package project.ForumWebApp.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.ForumWebApp.filterSpecifications.PostFilterSpecification;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.models.DTOs.PostCreateDTO;
import project.ForumWebApp.models.DTOs.PostDTO;
import project.ForumWebApp.models.DTOs.PostSummaryDTO;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.repository.UserRepository;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final TagService tagService;

    @Autowired
    public PostServiceImpl(TagService tagService, PostRepository postRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.tagService = tagService;
    }

    @Override
    @Transactional
    public PostDTO createPost(PostCreateDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        post.setUser(userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new RuntimeException("User not found")));
        Set<Tag> tags = new HashSet<>();
        if (postDTO.getTagNames() != null) {
            for (String tagName : postDTO.getTagNames()) {
                Optional<Tag> tagOptional = tagService.findTagByName(tagName);
                Tag tag;
                if (!tagOptional.isPresent()) {
                    tag = tagService.createTagByName(tagName);
                    tag.getPosts().add(post);
                    tagService.updateTag(tag);
                } else {
                    tag = tagService.addPostToTag(tagName, post);
                }
                tags.add(tag);
            }

            post.setTags(tags);
        }
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    @Transactional
    public PostDTO updatePost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(userRepository.findByUsername(postDTO.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found")));
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    @Transactional
    public void deletePost(int id) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            for(Tag tag : post.get().getTags()) {
                tag.getPosts().remove(post.get());
                tagService.updateTag(tag);
            }
        }
        postRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PostDTO> getPost(int id) {
        return postRepository.findById(id)
                .map(post -> modelMapper.map(post, PostDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getPosts(String title, String description, String user, List<String> tags, String sort) {
        Specification<Post> spec = PostFilterSpecification.withFiltersAndSort(title, description, user, tags, sort);
        List<Post> posts = postRepository.findAll(spec);

        return posts.stream()
                .map(post -> {
                    PostSummaryDTO dto = modelMapper.map(post, PostSummaryDTO.class);
                    dto.setCommentCount(post.getComments().size());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
