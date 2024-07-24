package project.ForumWebApp.services.Implementations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.repository.TagRepository;
import project.ForumWebApp.services.TagService;

@Service
public class TagServiceImpl implements TagService {
    
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    } 

    @Override
    public Tag createTagByName(String name) {
        return new Tag(0, name, new HashSet());
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        tag = tagRepository.save(tag);
        return tag;
    }

    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        tag = tagRepository.save(tag);
        return tag;
    }

    @Override
    public Tag addPostToTag(String name, Post post) {
        Optional<Tag> tagOptional = tagRepository.findTagByName(name);
        if (tagOptional.isPresent()) {
            Tag tag = tagOptional.get();
            tag.getPosts().add(post);
            return tagRepository.save(tag);
        }
        throw new RuntimeException("Tag not found with name " + name);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag get(int id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public void deleteTag(int id) {
        tagRepository.deleteById(id);
    }
}
