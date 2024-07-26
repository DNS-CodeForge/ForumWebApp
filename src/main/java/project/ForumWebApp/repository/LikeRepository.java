package project.ForumWebApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import project.ForumWebApp.models.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findByPostId(int postId);
    void deleteByPostId(int postId);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.post.id = :postId")
    Optional<Like> findLikeByUserIdAndPostId(int userId, int postId);
}
