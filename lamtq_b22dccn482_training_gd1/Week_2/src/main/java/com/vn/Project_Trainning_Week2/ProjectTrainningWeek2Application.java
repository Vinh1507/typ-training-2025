package com.vn.Project_Trainning_Week2;

import com.vn.Project_Trainning_Week2.constant.Gender;
import com.vn.Project_Trainning_Week2.entity.User;
import com.vn.Project_Trainning_Week2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ProjectTrainningWeek2Application {

	public static void main(String[] args) {
		SpringApplication.run(ProjectTrainningWeek2Application.class, args);
	}

	@Bean
	CommandLineRunner dataInitializer(UserRepository userRepository) {
		return args -> {
			if (userRepository.count() == 0) {
				User user1 = new User("Alice Smith", "123 Main St", Gender.FEMALE);
				User user2 = new User("Bob Johnson", "456 Oak Ave", Gender.MALE);
				User user3 = new User("Charlie Brown", "789 Pine Ln", Gender.MALE);
				User user4 = new User("Diana Prince", "101 Comic Rd", Gender.FEMALE);
				User user5 = new User("Ethan Hunt", "202 IMF Base", Gender.MALE);
				userRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));

			}
		};
	}
}