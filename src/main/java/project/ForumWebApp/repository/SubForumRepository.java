package project.ForumWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.ForumWebApp.models.SubForum;

@Repository
public interface SubForumRepository extends JpaRepository<SubForum, Integer>{
}
