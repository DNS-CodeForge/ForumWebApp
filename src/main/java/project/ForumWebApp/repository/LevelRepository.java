package project.ForumWebApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.ForumWebApp.models.LevelInfo;

public interface LevelRepository extends JpaRepository<LevelInfo, Integer>{
}
