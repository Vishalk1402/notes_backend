package com.notesapp.notesplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotesplatformApplication {

	public static void main(String[] args) {
		// Only load .env locally if environment variables are not set
		if (System.getProperty("DB_URL") == null) {
			try {
				io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.load();
				dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
			} catch (Exception e) {
				System.out.println(".env file not found, continuing with system environment variables");
			}
		}

		SpringApplication.run(NotesplatformApplication.class, args);
	}
}
