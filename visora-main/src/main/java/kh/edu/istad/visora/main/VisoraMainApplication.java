package kh.edu.istad.visora.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = {"kh.edu.istad.visora.data"})
@EntityScan(basePackages = {"kh.edu.istad.visora.data"})
@SpringBootApplication(scanBasePackages = {"kh.edu.istad.visora"})
public class VisoraMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(VisoraMainApplication.class, args);
    }

}

