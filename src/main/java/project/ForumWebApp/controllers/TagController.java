package project.ForumWebApp.controllers;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.ForumWebApp.models.Tag;
import project.ForumWebApp.services.TagService;

@RestController
@RequestMapping("api/tag")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all tags", description = "Fetches a list of all tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tags retrieved successfully")
    })
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific tag by ID", description = "Fetches the details of a specific tag based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found")
    })
    public Tag getById(@PathVariable int id) {
        return tagService.get(id);
    }

    @PostMapping
    @Operation(summary = "Create a new tag", description = "Creates a new tag")
    @RequestBody(description = "Tag details to be created", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a tag", description = "Deletes a specific tag based on the provided ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Tag not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public void deleteTag(@PathVariable int id) {
        tagService.deleteTag(id);
    }
}
