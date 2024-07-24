package project.ForumWebApp.services;

import jakarta.transaction.Transactional;
import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    Tag createTagByName(String name);

    Optional<Tag> findTagByName(String name);

    @Transactional
    Tag updateTag(Tag tag);

    @Transactional
    Tag createTag(Tag tag);

    Tag addPostToTag(String name, Post post);

    List<Tag> getAll();

    Tag get(int id);

    void deleteTag(Tag tag);

    void deleteTag(int id);
}

