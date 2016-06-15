package ua.epam.spring.hometask.service.strategy.impl;

import java.util.Objects;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.service.strategy.EventAdditionalPriceStrategy;

public class HighRatingEventPriceStrategy implements EventAdditionalPriceStrategy {

	private double highRatingMultiplier;
	
	@Override
	public double getAdditionaPrice(Event event, Auditorium auditorium, long seat) {
		if (isHighRatedEvent(event)) {
			return getAdditionalPrice(event.getBasePrice());
		}
		return 0;
	}
	
    private double getAdditionalPrice(double basePrice) {
        return Math.abs(highRatingMultiplier * basePrice - basePrice);
    }
	
	private boolean isHighRatedEvent(Event event) {
		return Objects.equals(EventRating.HIGH, event.getRating());
	}

	public void setHighRatingMultiplier(double highRatingMultiplier) {
		this.highRatingMultiplier = highRatingMultiplier;
	}

}
