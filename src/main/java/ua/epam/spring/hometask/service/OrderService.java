package ua.epam.spring.hometask.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.User;

public interface OrderService {
	
	Order createOrder(User user, List<OrderEntry> orderEntries);

	Order getOrder(long id);
	
	Collection<Order> getUserOrders(User user);
	
	double getOrderPrice(Order order);
	
	Set<OrderEntry> getOrderEntries(Order order);

}
