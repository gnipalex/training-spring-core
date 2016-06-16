package ua.epam.spring.hometask.dao;

import java.util.Collection;

import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.User;

public interface OrderDao extends DomainObjectDao<Order> {
	
	Collection<Order> getOrdersForUser(User user);

}
