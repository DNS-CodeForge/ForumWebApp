package project.ForumWebApp.services.Implementations;

import static project.ForumWebApp.constants.ValidationConstants.POST_WITH_PROVIDED_ID_DOES_NOT_EXIST;
import static project.ForumWebApp.constants.ValidationConstants.POST_WITH_THIS_TITLE_ALREADY_EXISTS;
import static project.ForumWebApp.constants.ValidationConstants.YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_POST;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.exceptions.AuthorizationException;
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
        if (postRepository.findByTitle(postDTO.getTitle()).isPresent()) {

            throw new EntityExistsException(POST_WITH_THIS_TITLE_ALREADY_EXISTS);
        }
        Post post = modelMapper.map(postDTO, Post.class);
        post.setUser(authContextManager.getLoggedInUser());
        Set<Tag> tags = new HashSet<>();
        if (postDTO.getTagNames() != null) {
            for (String tagName : postDTO.getTagNames()) {
                Optional<Tag> tagOptional = tagService.findTagByName(tagName);
                Tag tag;
                tag = tagOptional.orElseGet(() -> tagService.createTagByName(tagName));
                tag.getPosts().add(post);
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
                .orElseThrow(() -> new EntityNotFoundException(POST_WITH_PROVIDED_ID_DOES_NOT_EXIST));

        if (!post.getUser().getUsername().equals(authContextManager.getUsername())) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_POST);
        }

        post.setTitle(postUpdateDTO.getTitle());
        post.setDescription(postUpdateDTO.getDescription());

        Set<Tag> newTags = new HashSet<>();
        if (postUpdateDTO.getTags() != null) {
            for (String tagName : postUpdateDTO.getTags()) {
                Optional<Tag> tagOptional = tagService.findTagByName(tagName);
                Tag tag;
                tag = tagOptional.orElseGet(() -> tagService.createTagByName(tagName));
                tag.getPosts().add(post);
                newTags.add(tag);
            }
            Set<Tag> oldTags = new HashSet<>(post.getTags());
            for (Tag oldTag : oldTags) {
                if (!newTags.contains(oldTag)) {
                    oldTag.getPosts().remove(post);
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
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(POST_WITH_PROVIDED_ID_DOES_NOT_EXIST));

        if (!post.getUser().getUsername().equals(authContextManager.getUsername())) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_POST);
        }

        for (Tag tag : post.getTags()) {
            tag.getPosts().remove(post);
        }

        commentRepository.deleteByPostId(id);
        likeRepository.deleteByPostId(id);
        postRepository.deleteById(id);
    }

    public PostDTO getPost(int id) {
        return postRepository.findById(id)
                .map(post -> modelMapper.map(post, PostDTO.class))
                .orElseThrow(() -> new EntityNotFoundException(POST_WITH_PROVIDED_ID_DOES_NOT_EXIST));
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
    public List<PostSummaryDTO> getPosts(String title, String description, List<String> tags, String sort) {
        return getPosts(title, description, null, tags, sort);
    }

    @Override
    public boolean isOwner(int postId) {
        String currentUsername = authContextManager.getUsername();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(POST_WITH_PROVIDED_ID_DOES_NOT_EXIST));
        if (!post.getUser().getUsername().equals(currentUsername)) {
            throw new AuthorizationException(YOU_ARE_NOT_AUTHORIZED_TO_UPDATE_THIS_POST);
        }
        return true;
    }
}
