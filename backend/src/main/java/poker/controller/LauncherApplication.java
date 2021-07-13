package poker.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "poker")
@EnableScheduling
public class LauncherApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(LauncherApplication.class);
    }
}
