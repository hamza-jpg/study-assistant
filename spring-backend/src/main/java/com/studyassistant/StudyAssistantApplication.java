package com.studyassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyAssistantApplication.class, args);
    }
}
