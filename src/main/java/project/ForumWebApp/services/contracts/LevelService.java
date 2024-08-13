package project.ForumWebApp.services.contracts;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.LevelInfo;

public interface LevelService{
    void addExp(ApplicationUser applicationUser, int exp);
    LevelInfo getLevelById(int id);
}
