package project.ForumWebApp.services;

import project.ForumWebApp.models.ApplicationUser;

public interface LevelService{
    void addExp(ApplicationUser applicationUser, int exp);
}
