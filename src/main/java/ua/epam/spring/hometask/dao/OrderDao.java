package ua.epam.spring.hometask.dao;

import java.util.Set;

import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.User;

public interface OrderDao extends DomainObjectDao<Order> {
	
	Set<Order> getOrdersForUser(User user);

}
