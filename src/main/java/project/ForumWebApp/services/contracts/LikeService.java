package project.ForumWebApp.services.contracts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;

public interface LikeService{
    void likePost(int postId);
    boolean hasCurrentUserLikedPost(int postId);
    Page<PostSummaryDTO> getAllPostsLikedByUser(ApplicationUser user, Pageable pageable); 
}
