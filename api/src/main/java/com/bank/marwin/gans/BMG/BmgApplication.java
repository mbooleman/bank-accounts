package com.bank.marwin.gans.BMG;

import com.bank.marwin.gans.commands.CreateBankAccountCommand;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
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

    @Bean
    public CreateBankAccountCommand configureCreateBankAccountCommand() {
        return new CreateBankAccountCommand();
    }
}