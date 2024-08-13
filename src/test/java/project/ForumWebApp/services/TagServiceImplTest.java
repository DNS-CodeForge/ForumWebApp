package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.ForumWebApp.models.Post;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.repository.TagRepository;
import project.ForumWebApp.services.Implementations.TagServiceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private Post post;

    @BeforeEach
    void setUp() {
        tag = new Tag(1, "Tag1", new HashSet<>());
        post = new Post();
    }

    @Test
    void createTagByName_TagExists() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.of(tag));

        EntityExistsException thrown = assertThrows(EntityExistsException.class, () -> {
            tagService.createTagByName("Tag1");
        });

        assertEquals("Tag with provided name already exists.", thrown.getMessage());
    }

    @Test
    void createTagByName_TagDoesNotExist() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTagByName("Tag1");

        assertEquals(tag, createdTag);
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void findTagByName_TagFound() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.of(tag));

        Optional<Tag> foundTag = tagService.findTagByName("Tag1");

        assertTrue(foundTag.isPresent());
        assertEquals(tag, foundTag.get());
    }

    @Test
    void findTagByName_TagNotFound() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.empty());

        Optional<Tag> foundTag = tagService.findTagByName("Tag1");

        assertFalse(foundTag.isPresent());
    }

    @Test
    void updateTag_TagExists() {
        when(tagRepository.existsById(1)).thenReturn(true);
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag updatedTag = tagService.updateTag(tag);

        assertEquals(tag, updatedTag);
        verify(tagRepository).save(tag);
    }

    @Test
    void updateTag_TagNotFound() {
        when(tagRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            tagService.updateTag(tag);
        });

        assertEquals("Tag with ID does not exist.", thrown.getMessage());
    }

    @Test
    void createTag_TagExists() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.of(tag));

        EntityExistsException thrown = assertThrows(EntityExistsException.class, () -> {
            tagService.createTag(tag);
        });

        assertEquals("Tag with provided name already exists.", thrown.getMessage());
    }

    @Test
    void createTag_TagDoesNotExist() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag createdTag = tagService.createTag(tag);

        assertEquals(tag, createdTag);
        verify(tagRepository).save(tag);
    }

    @Test
    void addPostToTag_TagExists() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.of(tag));
        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        Tag updatedTag = tagService.addPostToTag("Tag1", post);

        assertTrue(updatedTag.getPosts().contains(post));
        verify(tagRepository).save(tag);
    }

    @Test
    void addPostToTag_TagNotFound() {
        when(tagRepository.findTagByName("Tag1")).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            tagService.addPostToTag("Tag1", post);
        });

        assertEquals("Tag with provided name does not exist.", thrown.getMessage());
    }

    @Test
    void getAllTags() {
        List<Tag> tags = List.of(tag);
        when(tagRepository.findAll()).thenReturn(tags);

        List<Tag> result = tagService.getAll();

        assertEquals(tags, result);
    }

    @Test
    void getTagById_TagFound() {
        when(tagRepository.findById(1)).thenReturn(Optional.of(tag));

        Tag foundTag = tagService.get(1);

        assertEquals(tag, foundTag);
    }

    @Test
    void getTagById_TagNotFound() {
        when(tagRepository.findById(1)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            tagService.get(1);
        });

        assertEquals("Tag with ID does not exist.", thrown.getMessage());
    }

    @Test
    void deleteTag_TagExists() {
        when(tagRepository.existsById(1)).thenReturn(true);

        tagService.deleteTag(tag);

        verify(tagRepository).delete(tag);
    }

    @Test
    void deleteTag_TagNotFound() {
        when(tagRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            tagService.deleteTag(tag);
        });

        assertEquals("Tag with ID does not exist.", thrown.getMessage());
    }

    @Test
    void deleteTagById_TagExists() {
        when(tagRepository.existsById(1)).thenReturn(true);

        tagService.deleteTag(1);

        verify(tagRepository).deleteById(1);
    }

    @Test
    void deleteTagById_TagNotFound() {
        when(tagRepository.existsById(1)).thenReturn(false);

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            tagService.deleteTag(1);
        });

        assertEquals("Tag with ID does not exist.", thrown.getMessage());
    }
}
