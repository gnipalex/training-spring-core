package ua.epam.spring.hometask.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import ua.epam.spring.hometask.aspect.CounterAspect;

@Configuration
@EnableAspectJAutoProxy
public class AspectsContext {
    
    @Bean
    public CounterAspect counterAspect() {
        return new CounterAspect();
    }

}
