package com.zoomout.myservice;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyServiceApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication.run(MyServiceApplication.class, args);
    }

}
