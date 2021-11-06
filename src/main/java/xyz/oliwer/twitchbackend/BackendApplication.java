package xyz.oliwer.twitchbackend;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

/**
 * This class represents the main (application) for this app.
 *
 * @author Oliwer - https://www.github.com/ImOliwer
 */
@SpringBootApplication
public class BackendApplication {
	/**
	 * Invoked upon application start.
	 */
	public static void main(String[] args) {
		run(BackendApplication.class, args);
	}
}