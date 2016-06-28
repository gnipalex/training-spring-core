package ua.epam.spring.hometask.context;

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ua.epam.spring.hometask.service.strategy.EventAdditionalPriceStrategy;
import ua.epam.spring.hometask.service.strategy.impl.HighRatingEventPriceStrategy;
import ua.epam.spring.hometask.service.strategy.impl.SummingAdditionalEventPriceStrategy;
import ua.epam.spring.hometask.service.strategy.impl.VipSeatsEventPriceStrategy;

@Configuration
@ComponentScan(basePackages = {"ua.epam.spring.hometask.service"})
public class ServicesContext {
    
    @Resource
    private EventAdditionalPriceStrategy vipSeatsEventPriceStrategy;
    @Resource
    private EventAdditionalPriceStrategy highRatingEventPriceStrategy;
    
    @Bean
    public EventAdditionalPriceStrategy vipSeatsEventPriceStrategy(
            @Value("${price.strategy.vip.seats.multiplier:2}") double multiplier) {
        VipSeatsEventPriceStrategy priceStrategy = new VipSeatsEventPriceStrategy(multiplier);
        return priceStrategy;
    }
    
    @Bean
    public EventAdditionalPriceStrategy highRatingEventPriceStrategy(
            @Value("${price.strategy.high.rating.multiplier:1.2}") double multiplier) {
        HighRatingEventPriceStrategy priceStrategy = new HighRatingEventPriceStrategy();
        priceStrategy.setHighRatingMultiplier(multiplier);
        return priceStrategy;
    }
    
    @Bean
    public EventAdditionalPriceStrategy summingAdditionalEventPriceStrategy() {
        SummingAdditionalEventPriceStrategy priceStrategy = new SummingAdditionalEventPriceStrategy();
        priceStrategy.setStrategies(Arrays.asList(vipSeatsEventPriceStrategy, highRatingEventPriceStrategy));
        return priceStrategy;
    }
    
    //services are defined via anotations

}
