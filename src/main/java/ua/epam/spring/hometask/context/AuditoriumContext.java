package ua.epam.spring.hometask.context;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import ua.epam.spring.hometask.domain.Auditorium;

@Configuration
@PropertySource(value = {"classpath:auditorium.properties"})
public class AuditoriumContext {
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    
    @Bean
    public Auditorium auditorium1(@Value("${auditorium.1.code}") String code, 
            @Value("${auditorium.1.name}") String name, 
            @Value("${auditorium.1.numberOfSeats}") long numberOfSeats, 
            @Value("${auditorium.1.vipSeats}") Set<Long> vipSeats) {
        return new Auditorium(name, code, numberOfSeats, vipSeats);
    }
    
    @Bean
    public Auditorium auditorium2(@Value("${auditorium.2.code}") String code, 
            @Value("${auditorium.2.name}") String name, 
            @Value("${auditorium.2.numberOfSeats}") long numberOfSeats, 
            @Value("${auditorium.2.vipSeats}") Set<Long> vipSeats) {
        return new Auditorium(name, code, numberOfSeats, vipSeats);
    }
    
    @Bean
    public Auditorium auditorium3(@Value("${auditorium.3.code}") String code, 
            @Value("${auditorium.3.name}") String name, 
            @Value("${auditorium.3.numberOfSeats}") long numberOfSeats, 
            @Value("${auditorium.3.vipSeats}") Set<Long> vipSeats) {
        return new Auditorium(name, code, numberOfSeats, vipSeats);
    }
   
}
