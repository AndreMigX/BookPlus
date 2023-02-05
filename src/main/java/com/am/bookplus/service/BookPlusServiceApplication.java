package com.am.bookplus.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class BookPlusServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(BookPlusServiceApplication.class, args);
    }

}
