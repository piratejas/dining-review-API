package com.piratejas.diningReviewAPI;

import com.piratejas.diningReviewAPI.models.Role;
import com.piratejas.diningReviewAPI.models.User;
import com.piratejas.diningReviewAPI.repositories.RoleRepository;
import com.piratejas.diningReviewAPI.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DiningReviewApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiningReviewApiApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			roleRepository.save(new Role("USER"));

			Set<Role> roles = new HashSet<>();
			roles.add(adminRole);

			User admin = new User(1L, "admin", passwordEncode.encode("password"), roles);

			userRepository.save(admin);
		};
	}
}
