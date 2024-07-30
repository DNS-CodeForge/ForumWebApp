package project.ForumWebApp.services.Implementations;

import org.springframework.stereotype.Service;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.LevelInfo;
import project.ForumWebApp.repository.LevelRepository;
import project.ForumWebApp.services.LevelService;

@Service
public class LevelServiceImpl implements LevelService{
    private final LevelRepository levelRepository;

    public LevelServiceImpl(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public void addExp(ApplicationUser applicationUser, int exp) {
        LevelInfo levelInfo = applicationUser.getLevelInfo();
        levelInfo.setCurrentExp(levelInfo.getCurrentExp() + exp);
        if(levelInfo.getCurrentExp() >= levelInfo.getExpToNextLevel()) {
            levelInfo.setCurrnetLevel(levelInfo.getCurrnetLevel() + 1);
            levelInfo.setCurrentExp(levelInfo.getCurrentExp() - levelInfo.getExpToNextLevel());
            levelInfo.setExpToNextLevel(levelInfo.getExpToNextLevel() * 2);
        } else if(levelInfo.getCurrentExp() < 0) {
            levelInfo.setCurrentExp(0);
        }
        levelRepository.save(levelInfo);
    }
}