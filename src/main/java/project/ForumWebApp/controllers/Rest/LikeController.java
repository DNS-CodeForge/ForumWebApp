package project.ForumWebApp.controllers.Rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.services.contracts.LikeService;

@RestController
public class LikeController{

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    @PostMapping("api/like/post/{id}")
    public void LikePost(@PathVariable Integer id) {
        likeService.likePost(id);
    }
}
