package com.students.jwsystem_public;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class JwsystemPublicApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwsystemPublicApplication.class, args);
    }

}
