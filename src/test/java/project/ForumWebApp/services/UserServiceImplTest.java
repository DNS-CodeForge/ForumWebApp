package project.ForumWebApp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.Implementations.UserServiceImpl;
import project.ForumWebApp.services.LevelService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthContextManager authContextManager;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LevelService levelService;

    @InjectMocks
    private UserServiceImpl userService;

    private ApplicationUser applicationUser;
    private UpdateUserDTO updateUserDTO;

    @BeforeEach
    void setUp() {
        applicationUser = new ApplicationUser();
        applicationUser.setUsername("testuser");
        applicationUser.setEmail("testuser@example.com");
        applicationUser.setPassword("encodedPassword");

        updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("NewFirstName");
        updateUserDTO.setLastName("NewLastName");
        updateUserDTO.setPassword("newPassword");

        Role userRole = new Role();
        userRole.setAuthority("USER");
        Set<Role> authorities = new HashSet<>();
        authorities.add(userRole);
        applicationUser.setAuthorities(authorities);
    }

    @Test
    void loadUserByUsername_UserFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(applicationUser));

        ApplicationUser userDetails = (ApplicationUser) userService.loadUserByUsername("testuser");

        assertEquals(applicationUser, userDetails);

    }

    @Test
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.loadUserByUsername("testuser"));
    }

    @Test
    void updateUserInfo() {
        when(authContextManager.getLoggedInUser()).thenReturn(applicationUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        UpdateUserDTO updatedInfo = userService.updateUserInfo(updateUserDTO);

        assertEquals("NewFirstName", applicationUser.getFirstName());
        assertEquals("NewLastName", applicationUser.getLastName());
        assertEquals("encodedNewPassword", applicationUser.getPassword());
        assertEquals(updateUserDTO, updatedInfo);

        verify(userRepository).save(applicationUser);
    }

    @Test
    void viewUserInfo() {
        when(authContextManager.getLoggedInUser()).thenReturn(applicationUser);
        RegistrationDTO expectedDTO = new RegistrationDTO();
        when(modelMapper.map(applicationUser, RegistrationDTO.class)).thenReturn(expectedDTO);

        RegistrationDTO userInfo = userService.viewUserInfo();

        assertEquals(expectedDTO, userInfo);
        assertEquals("*******", applicationUser.getPassword());
    }

    @Test
    void getUserById_UserFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(applicationUser));

        ApplicationUser user = userService.getUserById(1);

        assertEquals(applicationUser, user);
    }

    @Test
    void getUserById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void getUserByName_UserFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(applicationUser));

        ApplicationUser user = userService.getUserByName("testuser");

        assertEquals(applicationUser, user);
    }

    @Test
    void getUserByName_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserByName("testuser"));
    }

    @Test
    void getAllUsers() {
        List<ApplicationUser> users = List.of(applicationUser);
        when(userRepository.findAll()).thenReturn(users);

        List<ApplicationUser> result = userService.getAllUsers();

        assertEquals(users, result);
    }

    @Test
    void deleteUser_UserExists() {
        when(userRepository.existsById(1)).thenReturn(true);

        userService.deleteUser(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void deleteUser_UserNotFound() {
        when(userRepository.existsById(1)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1));
    }

    @Test
    void setUserRole_UserFound_RoleAdded_RoleRemoved() {
        Role userRole = new Role();
        userRole.setAuthority("USER");
        Role newRole = new Role();
        newRole.setAuthority("ADMIN");

        Set<Role> authorities = new HashSet<>(applicationUser.getAuthoritySet());
        authorities.add(userRole);
        applicationUser.setAuthorities(authorities);

        when(userRepository.findById(1)).thenReturn(Optional.of(applicationUser));
        when(roleRepository.findByAuthority("ADMIN")).thenReturn(Optional.of(newRole));

        ApplicationUser result = userService.setUserRole(1, "ADMIN", null);

        assertTrue(result.getAuthorities().contains(newRole));
        verify(userRepository).save(applicationUser);
    }

    @Test
    void setUserRole_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.setUserRole(1, "ADMIN", null));
    }

    @Test
    void setUserRole_RoleNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(applicationUser));
        when(roleRepository.findByAuthority("ADMIN")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.setUserRole(1, "ADMIN", null));
    }

    @Test
    void setUserRole_RoleRemovedNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(applicationUser));
        when(roleRepository.findByAuthority("ADMIN")).thenReturn(Optional.of(new Role()));

        when(roleRepository.findByAuthority("NON_EXISTENT_ROLE")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.setUserRole(1, "ADMIN", "NON_EXISTENT_ROLE"));
    }

}
