package com.speedment.jpastreamer.demo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.speedment.jpastreamer.demo.spring", "com.speedment.jpastreamer"})
public class JPAStreamerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JPAStreamerDemoApplication.class);
    }
    
}
