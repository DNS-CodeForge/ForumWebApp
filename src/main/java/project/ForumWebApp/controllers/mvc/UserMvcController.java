package project.ForumWebApp.controllers.mvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.LevelInfo;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.services.contracts.LevelService;
import project.ForumWebApp.services.contracts.PostService;
import project.ForumWebApp.services.contracts.UserService;

@Controller
public class UserMvcController {

    private final UserService userService;
    private final AuthContextManager authContextManager;
    private final LevelService levelService;
    private final PostService postService;


    public UserMvcController(PostService postService, UserService userService, AuthContextManager authContextManager, LevelService levelService) {
        this.userService = userService;
        this.postService = postService;
        this.authContextManager = authContextManager;
        this.levelService = levelService;
    }

    @GetMapping("/profile/info")
    public ModelAndView getUserProfileInfo() {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        LevelInfo levelInfo = levelService.getLevelById(authContextManager.getId());

        PageRequest pageable = PageRequest.of(0, 10); 
        Page<PostSummaryDTO> posts = postService.getPosts(null, null, userProfile.getUsername(), null, null, pageable);

        ModelAndView modelAndView = new ModelAndView("/profile/info");
        modelAndView.addObject("userProfile", userProfile); 
        modelAndView.addObject("posts", posts);


        if (levelInfo != null) {
            modelAndView.addObject("levelInfo", levelInfo);
        }

        return modelAndView;    
    }

    @GetMapping("/profile/edit")
    public String getEditProfilePage(Model model){
        model.addAttribute("userProfile", authContextManager.getLoggedInUser());
        return "profile/editProfile";
    }

    @PostMapping("/profile/edit")
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

