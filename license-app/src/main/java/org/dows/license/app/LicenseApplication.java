package org.dows.license.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.dows.license")
public class LicenseApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(LicenseApplication.class);
        application.run(args);
    }
}
