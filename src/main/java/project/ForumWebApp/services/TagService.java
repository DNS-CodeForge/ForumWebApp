package project.ForumWebApp.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.repository.TagRepository;

@Service
public class TagService {
    
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    } 

    public Tag createTagByName(String name) {
        return new Tag(0, name, new HashSet());
    }

    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Transactional
    public Tag updateTag(Tag tag) {
        tag = tagRepository.save(tag);
        return tag;
    }

    @Transactional
    public Tag createTag(Tag tag) {
        tag = tagRepository.save(tag);
        return tag;
    }

    public Tag addPostToTag(String name, Post post) {
        Optional<Tag> tagOptional = tagRepository.findTagByName(name);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.getPosts().add(post);
            return tagRepository.save(tag);
        }
        throw new RuntimeException("Tag not found with name " + name);
    }

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag get(int id) {
        return tagRepository.findById(id).get();
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    public void deleteTag(int id) {
        tagRepository.deleteById(id);
    }
}
