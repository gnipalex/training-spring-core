package ua.epam.spring.hometask.service.strategy.impl;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.strategy.EventPriceStrategy;

public class VipSeatsEventPriceStrategy implements EventPriceStrategy {

    private double multiplier;
    
    @Override
    public double getPrice(Event event, Auditorium auditorium, Set<Long> seats) {
        int vipSeatsCount = CollectionUtils.intersection(auditorium.getVipSeats(), seats).size();
        return vipSeatsCount * getVipPrice(event.getBasePrice());
    }
    
    private double getVipPrice(double basePrice) {
        return multiplier * basePrice;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }
    
}
