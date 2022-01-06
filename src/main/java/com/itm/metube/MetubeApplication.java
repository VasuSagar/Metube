package com.itm.metube;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MetubeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetubeApplication.class, args);
    }

}
