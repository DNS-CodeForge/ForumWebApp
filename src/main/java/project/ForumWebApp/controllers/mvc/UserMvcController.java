package project.ForumWebApp.controllers.mvc;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.ForumWebApp.config.security.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.LevelInfo;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.models.DTOs.CommentDTO;
import project.ForumWebApp.models.DTOs.post.PostSummaryDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.services.contracts.CommentService;
import project.ForumWebApp.services.contracts.LevelService;
import project.ForumWebApp.services.contracts.LikeService;
import project.ForumWebApp.services.contracts.PostService;
import project.ForumWebApp.services.contracts.UserService;

@Controller
public class UserMvcController {

    private final UserService userService;
    private final AuthContextManager authContextManager;
    private final LevelService levelService;
    private final PostService postService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final RoleRepository roleRepository;


    public UserMvcController(CommentService commentService, LikeService likeService, PostService postService, UserService userService, AuthContextManager authContextManager, LevelService levelService, RoleRepository roleRepository) {
        this.userService = userService;
        this.likeService = likeService;
        this.postService = postService;
        this.authContextManager = authContextManager;
        this.levelService = levelService;
        this.commentService = commentService;
        this.roleRepository = roleRepository;
    }


    @GetMapping("/profile/info")
    public String getUserProfileInfo() {
        return "redirect:/profile/info/posts";
    }

    @GetMapping("/profile/info/posts")
    public ModelAndView getUserProfileInfoUserPosts() {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        LevelInfo levelInfo = levelService.getLevelById(authContextManager.getId());

        PageRequest pageable = PageRequest.of(0, 10);
        Page<PostSummaryDTO> posts = postService.getPosts(null, null, userProfile.getUsername(), null, null, pageable);

        ModelAndView modelAndView = new ModelAndView("/profile/info");
        modelAndView.addObject("userProfile", userProfile);
        modelAndView.addObject("posts", posts);
        modelAndView.addObject("isInfoPage", true);


        if (levelInfo != null) {
            modelAndView.addObject("levelInfo", levelInfo);
        }

        return modelAndView;
    }

    @GetMapping("/profile/info/liked")
    public ModelAndView getUserProfileInfoLikedPosts() {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        LevelInfo levelInfo = levelService.getLevelById(authContextManager.getId());

        PageRequest pageable = PageRequest.of(0, 10);
        Page<PostSummaryDTO> posts = likeService.getAllPostsLikedByUser(userProfile, pageable);


        ModelAndView modelAndView = new ModelAndView("/profile/info");
        modelAndView.addObject("userProfile", userProfile);
        modelAndView.addObject("posts", posts);


        if (levelInfo != null) {
            modelAndView.addObject("levelInfo", levelInfo);
        }

        return modelAndView;
    }
    @ModelAttribute("loggedInUser")
    public ApplicationUser addUserToModel() {
        return authContextManager.getLoggedInUser();
    }

    @GetMapping("/profile/info/comments")
    public ModelAndView getUserProfileInfoLikedComments() {
        ApplicationUser userProfile = authContextManager.getLoggedInUser();
        LevelInfo levelInfo = levelService.getLevelById(authContextManager.getId());

        PageRequest pageable = PageRequest.of(0, 10);
        Page<CommentDTO> comments = commentService.getCommentsByUser(userProfile, pageable);


        ModelAndView modelAndView = new ModelAndView("/profile/info");
        modelAndView.addObject("userProfile", userProfile);
        modelAndView.addObject("comments", comments);

        if (levelInfo != null) {
            modelAndView.addObject("levelInfo", levelInfo);
        }

        return modelAndView;
    }


    @GetMapping("/profile/edit")
    public String getEditProfilePage(Model model) {
        model.addAttribute("userProfile", authContextManager.getLoggedInUser());
        return "/profile/edit-profile";
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

    @GetMapping("/manage-users")
    public String manageUsers(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {


        Page<ApplicationUser> users;
        Pageable pageable = PageRequest.of(page, size);

        if ((username != null && !username.isEmpty()) || (role != null && !role.isEmpty())) {
            users = userService.findUsersByUsernameAndRole(username, role, pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }

        model.addAttribute("username", username);
        model.addAttribute("role", role);
        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", users.getTotalPages());
        return "admin-panel";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/ban/{id}")
    public String banUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        try {
            userService.setUserRole(id, "BANNED", null);
            redirectAttributes.addFlashAttribute("successMessage", "User banned successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to ban user. Please try again.");
        }
        return "redirect:/manage-users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/unban/{id}")
    public String unbanUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {

        try {
            userService.setUserRole(id, null, "BANNED");
            redirectAttributes.addFlashAttribute("successMessage", "User banned successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to ban user. Please try again.");
        }
        return "redirect:/manage-users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/role/{id}")
    public String editUserRoles(@PathVariable Integer id, Model model) {
        ApplicationUser user = userService.findByID(id);


        List<String> allRoles = roleRepository.getAllRoles().stream().map(Role::getAuthority).toList();



        Set<String> userRoles = user.getAuthoritySet().stream()
                .map(Role::getAuthority)
                .collect(Collectors.toSet());


        List<String> rolesToAdd = allRoles.stream()
                .filter(role -> !userRoles.contains(role))
                .collect(Collectors.toList());


        List<String> rolesToRemove = userRoles.stream()
                .filter(role -> !role.equals("USER"))
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("rolesToAdd", rolesToAdd);
        model.addAttribute("rolesToRemove", rolesToRemove);

        return "admin-role-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users/role/{id}")
    public String updateUserRoles(
            @PathVariable Integer id,
            @RequestParam(required = false) String addedRole,
            @RequestParam(required = false) String removedRole,
            RedirectAttributes redirectAttributes) {

        addedRole = addedRole.isEmpty() ? null : addedRole;
        removedRole = removedRole.isEmpty() ? null : removedRole;
        try {
            userService.setUserRole(id, addedRole, removedRole);

            redirectAttributes.addFlashAttribute("successMessage", "User roles updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update user roles. Please try again.");
        }

        return "redirect:/manage-users";
    }

    @GetMapping("profile/select-avatar")
    public String showAvatarSelection(Model model) {
        model.addAttribute("level", levelService.getLevelById(authContextManager.getId()).getCurrentLevel());

        return "profile/select-avatar";
    }
    @PostMapping("profile/select-avatar")
    public String selectAvatar(@RequestParam("photoUrl") String photoUrl) {

        userService.updateUserProfileImage(photoUrl);

        return "redirect:/profile/edit";
    }


}

