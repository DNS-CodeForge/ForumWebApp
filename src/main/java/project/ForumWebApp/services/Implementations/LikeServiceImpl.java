package project.ForumWebApp.services.Implementations;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Like;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.repository.LikeRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.contracts.LevelService;
import project.ForumWebApp.services.contracts.LikeService;

@Service
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AuthContextManager authContextManager;
    private final LevelService levelService;
    private final ModelMapper modelMapper;

    public LikeServiceImpl(ModelMapper modelMapper, LevelService levelService, AuthContextManager authContextManager, LikeRepository likeRepository,PostRepository postRepository) {
        this.levelService = levelService;
        this.modelMapper = modelMapper;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.authContextManager = authContextManager;
    }

    @Override
    @Transactional
    public void likePost(int postId) {
        ApplicationUser loggedUser = authContextManager.getLoggedInUser();
        Post post = postRepository.findById(postId).get();

        Optional<Like> likeOptional = likeRepository.findLikeByUserIdAndPostId(loggedUser.getId(), postId); 

        if(!likeOptional.isPresent()) {
            Like like = new Like();
            like.setPost(post);
            like.setUser(loggedUser);
            post.getLikes().add(like);
            postRepository.save(post);
            levelService.addExp(post.getUser(), 1);
        } else {
            post.getLikes().remove(likeOptional.get());
            postRepository.save(post);
            levelService.addExp(post.getUser(), -1);
        }
   }

    @Override
    public boolean hasCurrentUserLikedPost(int postId) {
        if(authContextManager.getLoggedInUser() == null) {
            return false;
        }
        int currentUserId = authContextManager.getLoggedInUser().getId();
        Optional<Like> like = likeRepository.findLikeByUserIdAndPostId(currentUserId, postId);
        return like.isPresent();    
    }

    @Override
    @Transactional
      public Page<PostSummaryDTO> getAllPostsLikedByUser(ApplicationUser user, Pageable pageable) {
            Page<Like> likePage = likeRepository.findByUserId(user.getId(), pageable);
            
        List<PostSummaryDTO> likedPostsDto = likePage.getContent().stream()
        .map(like -> {
            PostSummaryDTO dto = modelMapper.map(like.getPost(), PostSummaryDTO.class);
            dto.setLikedByCurrentUser(true); // Set the flag here
            return dto;
        })
        .collect(Collectors.toList());

            return new PageImpl<>(likedPostsDto, pageable, likePage.getTotalElements());
        }
}
