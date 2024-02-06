package com.agap;

import com.agap.repository.ResourceRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableCaching
@ConfigurationPropertiesScan
@EnableMongoRepositories(repositoryBaseClass = ResourceRepositoryImpl.class)
public class MusicApp {

    public static void main(String[] args) {

        SpringApplication.run(MusicApp.class, args);
    }
}
