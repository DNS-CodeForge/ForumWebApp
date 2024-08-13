package project.ForumWebApp.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.models.LevelInfo;
import project.ForumWebApp.services.contracts.LevelService;
import project.ForumWebApp.services.contracts.UserService;

@Controller
public class UserMvcController {

    private final UserService userService;
    private final AuthContextManager authContextManager;
    private final LevelService levelService;


    public UserMvcController(UserService userService, AuthContextManager authContextManager, LevelService levelService) {
        this.userService = userService;
        this.authContextManager = authContextManager;
        this.levelService = levelService;
    }

    @GetMapping("/profile/info")
    public String getUserProfileInfo(Model model) {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        LevelInfo levelInfo = levelService.getLevelById(authContextManager.getId());

        model.addAttribute("userProfile", userProfile);
        if (levelInfo != null) {
            model.addAttribute("levelInfo", levelInfo);
        }
        return "profile/info";
    }

    @PostMapping("/profile/info")
    public String updateUserInfo(@ModelAttribute("userProfile") UpdateUserDTO updateUserDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            userService.updateUserInfo(updateUserDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile. Please try again.");
        }

        return "redirect:/profile/info";
    }
}

