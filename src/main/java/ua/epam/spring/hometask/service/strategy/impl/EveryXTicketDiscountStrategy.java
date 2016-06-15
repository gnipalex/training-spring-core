package ua.epam.spring.hometask.service.strategy.impl;

import java.time.LocalDateTime;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

public class EveryXTicketDiscountStrategy implements DiscountStrategy {

    private int number;
    private byte discountPercent;
    
    @Override
    public double getDiscount(User user, Event event, LocalDateTime airDateTime,
            long numberOfTickets) {
    	int discountApplyTimes = getDiscountApplyTimes(numberOfTickets);
    	
    	if (discountApplyTimes > 0 ) {
    		double discountPerTicket = (event.getBasePrice() * discountPercent) / 100;
			return discountPerTicket * discountApplyTimes;
		}
    	
    	return 0;
    }
    
    private int getDiscountApplyTimes(long numberOfTickets) {
    	return (int) numberOfTickets / number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

	public void setDiscountPercent(byte discountPercent) {
		this.discountPercent = discountPercent;
	}
    
}
