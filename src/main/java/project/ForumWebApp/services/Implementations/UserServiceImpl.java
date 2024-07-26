package project.ForumWebApp.services.Implementations;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.ForumWebApp.config.AuthContextManager;
import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.user.RegistrationDTO;
import project.ForumWebApp.models.DTOs.user.UpdateUserDTO;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.UserService;

import static project.ForumWebApp.constants.ValidationConstants.USER_WITH_PROVIDED_ID_DOES_NOT_EXIST;
import static project.ForumWebApp.constants.ValidationConstants.USER_WITH_PROVIDED_USERNAME_DOES_NOT_EXIST;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthContextManager authContextManager;

    @Autowired
    public UserServiceImpl(PasswordEncoder encoder, UserRepository userRepository, ModelMapper modelMapper, AuthContextManager authContextManager) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = encoder;
        this.authContextManager = authContextManager;
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
}
