package project.ForumWebApp.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.ForumWebApp.models.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    Optional<Tag> findTagByName(String name);
}
