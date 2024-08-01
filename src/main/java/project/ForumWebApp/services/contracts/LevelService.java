package project.ForumWebApp.services.contracts;

import project.ForumWebApp.models.ApplicationUser;

public interface LevelService{
    void addExp(ApplicationUser applicationUser, int exp);
}
