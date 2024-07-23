package project.ForumWebApp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ForumWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumWebAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args -> {
			System.out.println("\033[32m" +
					"**************************************************\n" +
					"*                                                *\n" +
					"*   Spring has loaded Successfully and is Ready. *\n" +
					"*                                                *\n" +
					"**************************************************\033[0m");
		};
		};
	}

