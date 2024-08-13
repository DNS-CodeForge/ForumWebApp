package project.ForumWebApp;


import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.models.Tag;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.TagRepository;
import project.ForumWebApp.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class ForumWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumWebAppApplication.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, TagRepository tagRepository) {
        return args -> {

            if (userRepository.findByUsername("admin").isPresent()) {
                System.out.println("\033[31m" +
                        "******************************************************************\n" +
                        "*                                                                *\n" +
                        "*     Admin account - username: admin  password: password        *\n" +
                        "*                                                                *\n" +
                        "******************************************************************\033[0m");
            }
            System.out.println();
            System.out.println();


                System.out.println("\033[32m" +
                        "******************************************************************\n" +
                        "*                                                                *\n" +
                        "*           Spring has loaded Successfully and is Ready.         *\n" +
                        "*                                                                *\n" +
                        "******************************************************************\033[0m");

        };
    }
}
