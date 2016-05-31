package com.rom.quizup.server;

import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point to the web based application.
 * 
 * When the Spring framework initializes, it loads
 * the @see {@link Tomcat} servlet container, loads the
 * configuration files and scan the code base for annotated
 * classes. Based on the annotations it configures for us
 * automatically all the APIs, services and repositories.
 * 
 * @author rom
 */
@SpringBootApplication
public class QuizupServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizupServerApplication.class, args);
	}
}
