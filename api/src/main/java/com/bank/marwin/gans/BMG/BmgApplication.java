package com.bank.marwin.gans.BMG;

import com.bank.marwin.gans.BMG.events.KafkaProperties;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(KafkaProperties.class)
public class BmgApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmgApplication.class, args);
    }

    @Configuration
    public class SwaggerConfig {
        @Bean
        public GroupedOpenApi publicApi() {
            return GroupedOpenApi.builder().group("public").pathsToMatch("/**").build();
        }
    }
}