package project.ForumWebApp.services.Implementations;

import static project.ForumWebApp.constants.ValidationConstants.USER_WITH_PROVIDED_ID_DOES_NOT_EXIST;
import static project.ForumWebApp.constants.ValidationConstants.USER_WITH_PROVIDED_USERNAME_DOES_NOT_EXIST;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.UserService;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthContextManager authContextManager;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(RoleRepository roleRepository, PasswordEncoder encoder, UserRepository userRepository, ModelMapper modelMapper, AuthContextManager authContextManager) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = encoder;
        this.authContextManager = authContextManager;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional
    public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO) {
        ApplicationUser user = authContextManager.getLoggedInUser();

        if (updateUserDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }
        if (updateUserDTO.getFirstName() != null) {
            user.setFirstName(updateUserDTO.getFirstName());
        }
        if (updateUserDTO.getLastName() != null) {
            user.setLastName(updateUserDTO.getLastName());
        }

        userRepository.save(user);

        return updateUserDTO;
    }

    @Override
    public RegistrationDTO viewUserInfo() {
        ApplicationUser user = authContextManager.getLoggedInUser();
        user.setPassword("*******");
        return modelMapper.map(user, RegistrationDTO.class);
    }

    @Override
    public ApplicationUser getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(USER_WITH_PROVIDED_ID_DOES_NOT_EXIST));
    }

    @Override
    public ApplicationUser getUserByName(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new EntityNotFoundException(USER_WITH_PROVIDED_USERNAME_DOES_NOT_EXIST));
    }

    @Override
    public List<ApplicationUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(USER_WITH_PROVIDED_ID_DOES_NOT_EXIST);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ApplicationUser setUserRole(int userId, String addedRoleName, String removedRoleName) {
        ApplicationUser user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Set<Role> authorities = user.getAuthoritySet();

        if (addedRoleName != null) {
            Role addedRole = roleRepository.findByAuthority(addedRoleName)
                    .orElseThrow(() -> new EntityNotFoundException("Role " + addedRoleName + " does not exist!"));
            authorities.add(addedRole);
        }

        if (removedRoleName != null && !removedRoleName.equalsIgnoreCase("USER")) {
            Role removedRole = roleRepository.findByAuthority(removedRoleName)
                    .orElseThrow(() -> new EntityNotFoundException("Role " + removedRoleName + " does not exist!"));
            authorities.remove(removedRole);
        }

        user.setAuthorities(authorities);
        userRepository.save(user);

        return user;
    }



}
