package project.ForumWebApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ForumWebApp.models.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findByPostId(int postId);
    void deleteByPostId(int postId);
}
