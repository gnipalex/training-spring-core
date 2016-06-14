package ua.epam.spring.hometask.dao;

import java.util.Set;

import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;

public interface OrderEntryDao extends DomainObjectDao<OrderEntry> {
    
    Set<OrderEntry> saveAll(Set<OrderEntry> orderEntry);

	void removeOrderEntriesForOrder(Order order);
	
	Set<OrderEntry> getOrderEntriesForOrder(Order order);
    
}
