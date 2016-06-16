package ua.epam.spring.hometask.dao;

import java.util.Set;

import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;

public interface OrderEntryDao extends DomainObjectDao<OrderEntry> {
    
	void removeOrderEntriesForOrder(Order order);
	
	Set<OrderEntry> getOrderEntriesForOrder(Order order);
	 
}
