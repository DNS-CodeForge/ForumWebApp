package project.ForumWebApp.services.Implementations;

import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Like;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.repository.LikeRepository;
import project.ForumWebApp.repository.PostRepository;
import project.ForumWebApp.services.LevelService;
import project.ForumWebApp.services.LikeService;

@Service
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final AuthContextManager authContextManager;
    private final LevelService levelService;

    public LikeServiceImpl(LevelService levelService, AuthContextManager authContextManager, LikeRepository likeRepository,PostRepository postRepository) {
        this.levelService = levelService;
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
            likeRepository.save(like);
            levelService.addExp(post.getUser(), 1);
        } else {
            post.getLikes().remove(likeOptional.get());
            postRepository.save(post);
            likeRepository.delete(likeOptional.get());
            levelService.addExp(post.getUser(), -1);
        }
   }
}
