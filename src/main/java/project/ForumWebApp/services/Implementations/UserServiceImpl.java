package project.ForumWebApp.services.Implementations;



import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.DTOs.UpdateUserDTO;
import project.ForumWebApp.repository.UserRepository;
import project.ForumWebApp.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PasswordEncoder encoder, UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }

    @Override
    @Transactional
    public UpdateUserDTO updateUserInfo(UpdateUserDTO updateUserDTO) {
        ApplicationUser user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();

        if(updateUserDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateUserDTO.getPassword()));
        }
        if(updateUserDTO.getFirstName() != null) {
            user.setFirstName(updateUserDTO.getFirstName());
        }
        if(updateUserDTO.getLastName() != null) {
            user.setLastName(updateUserDTO.getLastName());
        }

        userRepository.save(user);
        return updateUserDTO;
    }
}
