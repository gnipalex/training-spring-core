package ua.epam.spring.hometask.dao;

import java.util.Set;

import ua.epam.spring.hometask.domain.OrderEntry;

public interface OrderEntryDao extends DomainObjectDao<OrderEntry> {
    
    Set<OrderEntry> saveAll(Set<OrderEntry> orderEntry);
    
}
