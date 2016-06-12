package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

public class DefaultDiscountService implements DiscountService {

    private List<DiscountStrategy> discountStrategies = Collections.emptyList();
    
    @Override
    public byte getDiscount(User user, Event event, LocalDateTime airDateTime, long numberOfTickets) {
        List<Byte> discounts = evaluateDiscounts(user, event, airDateTime, numberOfTickets);
        return getMaxDiscount(discounts);
    }
    
    private Byte getMaxDiscount(List<Byte> discounts) {
        return discounts.stream().max(Byte::compare).get();
    }
    
    private List<Byte> evaluateDiscounts(User user, Event event, LocalDateTime airDateTime,
            long numberOfTickets) {
        return discountStrategies.stream()
            .map(strategy -> strategy.getDiscount(user, event, airDateTime, numberOfTickets))
            .collect(Collectors.toList());
    }

    public void setDiscountStrategies(List<DiscountStrategy> discountStrategies) {
        this.discountStrategies = discountStrategies;
    }
    
}
