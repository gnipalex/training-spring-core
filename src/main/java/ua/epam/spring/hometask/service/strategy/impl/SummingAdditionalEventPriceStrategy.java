package ua.epam.spring.hometask.service.strategy.impl;

import java.util.List;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.strategy.EventAdditionalPriceStrategy;

public class SummingAdditionalEventPriceStrategy implements EventAdditionalPriceStrategy {

	private List<EventAdditionalPriceStrategy> strategies;
	
	@Override
	public double getAdditionaPrice(Event event, Auditorium auditorium,
			long seat) {
		return strategies.stream().map(strategy -> strategy.getAdditionaPrice(event, auditorium, seat))
				.reduce(0D, (a, b) -> a + b);
	}

	public void setStrategies(List<EventAdditionalPriceStrategy> strategies) {
		this.strategies = strategies;
	}

}
