package ua.epam.spring.hometask.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import ua.epam.spring.hometask.aspect.CounterAspect;
import ua.epam.spring.hometask.aspect.DiscountAspect;
import ua.epam.spring.hometask.aspect.LuckyWinnerAspect;
import ua.epam.spring.hometask.service.strategy.LuckyUserStrategy;
import ua.epam.spring.hometask.service.strategy.impl.RandomLuckyUserStrategy;

@Configuration
@EnableAspectJAutoProxy
public class AspectsContext {
    
    @Bean
    public CounterAspect counterAspect() {
        return new CounterAspect();
    }
    
    @Bean
    public DiscountAspect discountAspect() {
        return new DiscountAspect();
    }
    
    @Bean
    public LuckyWinnerAspect luckyWinnerAspect() {
        return new LuckyWinnerAspect();
    }
    
    @Bean
    public LuckyUserStrategy luckyUserStrategy() {
        return new RandomLuckyUserStrategy();
    }

}
