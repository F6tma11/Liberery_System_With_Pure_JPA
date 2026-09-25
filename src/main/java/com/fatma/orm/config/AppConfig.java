package com.fatma.orm.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.fatma.orm")
public class AppConfig {

    @Bean("factory")
    public EntityManagerFactory getManagerEntity(){
        return Persistence.createEntityManagerFactory("bookDB");
    }


}
