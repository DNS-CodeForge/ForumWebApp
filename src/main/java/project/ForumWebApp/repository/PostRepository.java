package project.ForumWebApp.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.ForumWebApp.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title%")
    public List<Post> findByTitleContaining(@Param("title") String title);
}
