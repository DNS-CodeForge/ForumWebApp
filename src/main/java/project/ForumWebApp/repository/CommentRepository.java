package project.ForumWebApp.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
    List<Comment> findByPostId(int postId);
    void deleteByPostId(int postId);
    Page<Comment> findByUser(ApplicationUser user, Pageable pageable);
}
