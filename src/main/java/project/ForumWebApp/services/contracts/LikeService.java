package project.ForumWebApp.services.contracts;

public interface LikeService{
    void likePost(int postId);
    boolean hasCurrentUserLikedPost(int postId);
}
