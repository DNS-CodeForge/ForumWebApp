package project.ForumWebApp.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.ForumWebApp.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>{
}
