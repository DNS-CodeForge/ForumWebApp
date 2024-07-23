package project.ForumWebApp.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.models.Tag;
import project.ForumWebApp.services.TagService;

@RestController
public class TagController{
    private TagService tagService;
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

}
