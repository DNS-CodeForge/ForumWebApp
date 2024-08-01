package project.ForumWebApp.services.Implementations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.repository.TagRepository;
import project.ForumWebApp.services.contracts.TagService;

import static project.ForumWebApp.constants.ValidationConstants.*;

@Service
public class TagServiceImpl implements TagService {


    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public Tag createTagByName(String name) {
        if (tagRepository.findTagByName(name).isPresent()) {
            throw new EntityExistsException(TAG_WITH_PROVIDED_NAME_ALREADY_EXISTS);
        }
        Tag tag = new Tag(0, name, new HashSet<>());
        return tagRepository.save(tag);
    }

    @Override
    public Optional<Tag> findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Override
    @Transactional
    public Tag updateTag(Tag tag) {
        if (!tagRepository.existsById(tag.getId())) {
            throw new EntityNotFoundException(TAG_WITH_ID_DOES_NOT_EXIST);
        }
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag createTag(Tag tag) {
        if (tagRepository.findTagByName(tag.getName()).isPresent()) {
            throw new EntityExistsException(TAG_WITH_PROVIDED_NAME_ALREADY_EXISTS);
        }
        return tagRepository.save(tag);
    }

    @Override
    @Transactional
    public Tag addPostToTag(String name, Post post) {
        Tag tag = tagRepository.findTagByName(name)
                .orElseThrow(() -> new EntityNotFoundException(TAG_WITH_PROVIDED_NAME_DOES_NOT_EXIST));
        tag.getPosts().add(post);
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag get(int id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TAG_WITH_ID_DOES_NOT_EXIST));
    }

    @Override
    @Transactional
    public void deleteTag(Tag tag) {
        if (!tagRepository.existsById(tag.getId())) {
            throw new EntityNotFoundException(TAG_WITH_ID_DOES_NOT_EXIST);
        }
        tagRepository.delete(tag);
    }

    @Override
    @Transactional
    public void deleteTag(int id) {
        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException(TAG_WITH_ID_DOES_NOT_EXIST);
        }
        tagRepository.deleteById(id);
    }
}
