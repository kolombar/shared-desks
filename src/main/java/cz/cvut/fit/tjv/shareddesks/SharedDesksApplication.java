package cz.cvut.fit.tjv.shareddesks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;

@SpringBootApplication
public class SharedDesksApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedDesksApplication.class, args);
    }
}
