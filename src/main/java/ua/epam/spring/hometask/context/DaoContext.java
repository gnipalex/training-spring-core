package ua.epam.spring.hometask.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.SimpleIdGenerator;

@Configuration
@ComponentScan(basePackages = {"ua.epam.spring.hometask.dao.impl"})
public class DaoContext {
    
    @Bean
    @Scope(value = "prototype")
    public IdGenerator idGenerator() {
        return new SimpleIdGenerator();
    }

}
