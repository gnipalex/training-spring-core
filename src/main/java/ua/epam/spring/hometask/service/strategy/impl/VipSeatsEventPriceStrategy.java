package ua.epam.spring.hometask.service.strategy.impl;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.strategy.EventAdditionalPriceStrategy;

public class VipSeatsEventPriceStrategy implements EventAdditionalPriceStrategy {

    private double vipSeatPriceMultiplier;

    public VipSeatsEventPriceStrategy(double vipSeatPriceMultiplier) {
		this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
	}

	@Override
    public double getAdditionaPrice(Event event, Auditorium auditorium, long seat) {
    	if (isVipSeat(auditorium, seat)) {
    		return getVipAdditionalPrice(event.getBasePrice());
    	}
    	return 0;
    }
    
    private boolean isVipSeat(Auditorium auditorium, long seat) {
    	return auditorium.getVipSeats().contains(seat);
    }
    
    private double getVipAdditionalPrice(double basePrice) {
        return Math.abs(vipSeatPriceMultiplier * basePrice - basePrice);
    }
    
}
