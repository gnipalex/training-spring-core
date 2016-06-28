package ua.epam.spring.hometask.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ua.epam.spring.hometask.service.strategy.DiscountStrategy;
import ua.epam.spring.hometask.service.strategy.impl.BirthdayDiscountStrategy;
import ua.epam.spring.hometask.service.strategy.impl.EveryXTicketDiscountStrategy;

@Configuration
public class DiscountContext {

    @Value("${discount.strategy.birthday.within.days:5}")
    private int birthdayDiscountWithinDays;
    @Value("${discount.strategy.birthday.value:5}")
    private byte birthdayDiscountValue;
    
    @Bean
    public DiscountStrategy birthdayDiscountStrategy() {
        BirthdayDiscountStrategy discountStrategy = new BirthdayDiscountStrategy();
        discountStrategy.setDiscountValue(birthdayDiscountValue);
        discountStrategy.setWithinDays(birthdayDiscountWithinDays);
        return discountStrategy;
    }
    
    @Bean
    public DiscountStrategy everyXTicketDiscountStrategy(
            @Value("${discount.strategy.everyXTicket.number:10}") int number,
            @Value("${discount.strategy.everyXTicket.discountPercent:50}") byte discountPercent) {
        EveryXTicketDiscountStrategy discountStrategy = new EveryXTicketDiscountStrategy();
        discountStrategy.setDiscountPercent(discountPercent);
        discountStrategy.setNumber(number);
        return discountStrategy;
    }
    
}
