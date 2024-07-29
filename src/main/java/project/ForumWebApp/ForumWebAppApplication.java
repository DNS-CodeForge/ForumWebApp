package project.ForumWebApp;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import project.ForumWebApp.models.ApplicationUser;
import project.ForumWebApp.models.Role;
import project.ForumWebApp.repository.RoleRepository;
import project.ForumWebApp.repository.UserRepository;

@SpringBootApplication
public class ForumWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumWebAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
		return args -> {
        	if (roleRepository.findByAuthority("USER").isEmpty()){
				roleRepository.save(new Role("USER"));
			}

        	if (roleRepository.findByAuthority("MODERATOR").isEmpty()){
				roleRepository.save(new Role("MODERATOR"));
			}

			if (roleRepository.findByAuthority("ADMIN").isEmpty()){
				roleRepository.save(new Role("ADMIN"));
			}

			if (roleRepository.findByAuthority("BANNED").isEmpty()){
				roleRepository.save(new Role("BANNED"));
			}


			if(!userRepository.findByUsername("admin").isPresent()) {
				Set<Role> authorities = new HashSet<>();
				authorities.add(roleRepository.findById(3).get());

				ApplicationUser admin = new ApplicationUser(
						null,
						"adminFirst",
						"adminLast",
						"admin.admin@admin.com",
						passwordEncoder.encode("password"),
						"admin",
						authorities
				);
				userRepository.save(admin);
				System.out.println("\u001B[31m" + "A user ADMIN with username \"admin\" and password \"password\" has been created." + "\u001B[0m");
				System.out.println();
				System.out.println();

			}


			System.out.println("\033[32m" +
					"**************************************************\n" +
					"*                                                *\n" +
					"*   Spring has loaded Successfully and is Ready. *\n" +
					"*                                                *\n" +
					"**************************************************\033[0m");
		};
		};
	}

