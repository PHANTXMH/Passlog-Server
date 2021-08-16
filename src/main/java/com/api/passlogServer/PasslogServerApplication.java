package com.api.passlogServer;

import com.api.passlogServer.credential.CredentialService;
import com.api.passlogServer.credential.Credentials;
import com.api.passlogServer.user.Role;
import com.api.passlogServer.user.UserService;
import com.api.passlogServer.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SpringBootApplication @Slf4j
public class PasslogServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasslogServerApplication.class, args);
		log.info("APPLICATION IS NOW RUNNING");
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, CredentialService credentialService){
		userService.addRole(new Role("USER"));
		userService.addRole(new Role("ADMIN"));
		return args -> {
			userService.addUser(
					new Users("Marcal",
							"Harrison",
							"phantxmh",
							"1234",
							6)
			);
			userService.addUser(
					new Users("",
							"",
							"jdgirlie",
							"1234",
							2)
			);
			userService.addUser(
					new Users("John",
							"Doe",
							"jdboii",
							"1234",
							3)
			);
			userService.addRoleToUser("phantxmh","USER");
			userService.addRoleToUser("phantxmh","ADMIN");
			userService.addRoleToUser("jdboii", "USER");
			userService.addRoleToUser("jdgirlie", "USER");

			credentialService.addCredential(
					new Credentials("phantxmh","1234","","PASSLOG",1)
			);
			credentialService.addCredential(
					new Credentials("phantxmh","Rzer","Thin and light","RAZER",1)
			);
			credentialService.addCredential(
					new Credentials("","7777","","PHONE",1)
			);
			credentialService.addCredential(
					new Credentials("marcalharrison@gmail.com","password","","GMAIL",1)

			);
			credentialService.addCredential(
					new Credentials("jdboii","1234","Credential for passlog","PASSLOG",2)
			);
		};
	}
}
