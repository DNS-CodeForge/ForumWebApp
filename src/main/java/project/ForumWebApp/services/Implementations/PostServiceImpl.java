package project.ForumWebApp.services.Implementations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.filterSpecifications.PostFilterSpecification;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.models.DTOs.post.PostCreateDTO;
import project.ForumWebApp.models.DTOs.post.PostDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.post.PostUpdateDTO;
import project.ForumWebApp.repository.CommentRepository;
import project.ForumWebApp.repository.LikeRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.PostService;
import project.ForumWebApp.services.TagService;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final TagService tagService;
    private final AuthContextManager authContextManager;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public PostServiceImpl(TagService tagService, PostRepository postRepository, ModelMapper modelMapper, AuthContextManager authContextManager, CommentRepository commentRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.tagService = tagService;
        this.authContextManager = authContextManager;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    @Transactional
    public PostDTO createPost(PostCreateDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(authContextManager.getLoggedInUser());
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
    public PostDTO updatePost(int id, PostUpdateDTO postUpdateDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        post.setTitle(postUpdateDTO.getTitle());
        post.setDescription(postUpdateDTO.getDescription());

        Set<Tag> newTags = new HashSet<>();
        if (postUpdateDTO.getTags() != null) {
            for (String tagName : postUpdateDTO.getTags()) {
                Optional<Tag> tagOptional = tagService.findTagByName(tagName);
                Tag tag;
                if (!tagOptional.isPresent()) {
                    tag = tagService.createTagByName(tagName);
                } else {
                    tag = tagOptional.get();
                }
                tag.getPosts().add(post);
                newTags.add(tag);
                tagService.updateTag(tag);
            }
            Set<Tag> oldTags = new HashSet<>(post.getTags());
            for (Tag oldTag : oldTags) {
                if (!newTags.contains(oldTag)) {
                    oldTag.getPosts().remove(post);
                    tagService.updateTag(oldTag);
                }
            }
            post.setTags(newTags);
        }
        post = postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    @Transactional
    public void deletePost(int id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            for (Tag tag : post.get().getTags()) {
                tag.getPosts().remove(post.get());
                tagService.updateTag(tag);
            }
        }
        commentRepository.deleteByPostId(id);
        likeRepository.deleteByPostId(id);

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
    @Override
    @Transactional(readOnly = true)
    public List<PostSummaryDTO> getPosts(String title, String description, List<String> tags, String sort)
    {
        return getPosts(title, description, null, tags, sort);
    }

    @Override
    public boolean isOwner(int postId) {
        String currentUsername = authContextManager.getUsername();
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && post.getUser().getUsername().equals(currentUsername);
    }
}
