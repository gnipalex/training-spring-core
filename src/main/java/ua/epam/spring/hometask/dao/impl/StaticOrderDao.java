package ua.epam.spring.hometask.dao.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.dao.OriginalDomainObjectProvider;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.User;

public class StaticOrderDao implements OrderDao, OriginalDomainObjectProvider<Order> {

    private Set<Order> orders = new HashSet<>();
    
    private OrderEntryDao orderEntryDao;
    private OriginalDomainObjectProvider<OrderEntry> originalOrderEntryProvider;
    private IdGenerator idGenerator;
    
    @Override
    public Order save(Order newOrder) {
        Order savedOrder = new Order(newOrder);
        savedOrder.setId(idGenerator.generateNextId());
        Set<OrderEntry> savedEntries = orderEntryDao.saveAll(newOrder.getEntries());
        Set<OrderEntry> originalSavedEntries = getOriginalOrderEntries(savedEntries);
        savedOrder.setEntries(originalSavedEntries);
        orders.add(savedOrder);
        Order order = new Order(savedOrder);
        order.setEntries(savedEntries);
        return order;
    }
    
    private Set<OrderEntry> getOriginalOrderEntries(Set<OrderEntry> entries) {
        return entries.stream().map(originalOrderEntryProvider::getOriginalDomainObject).collect(Collectors.toSet());
    }

    @Override
    public void remove(Order object) {
        Order originalOrder = getOriginalDomainObject(object);
        if (!orders.contains(originalOrder)) {
            throw new IllegalArgumentException("order doesn't exist");
        }
        removeAllOrderEntries(originalOrder);
        orders.remove(originalOrder);
    }
    
    private void removeAllOrderEntries(Order order) {
        order.getEntries().forEach(oe -> orderEntryDao.remove(oe));
    }

    @Override
    public Order getById(long id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst().map(this::getOrderCopyWithDependencies).orElse(null);
    }
    
    private Order getOrderCopyWithDependencies(Order order) {
        Order copyOfOrder = new Order(order);
        copyOfOrder.setEntries(getOrderEntriesCopy(order.getEntries()));
        return copyOfOrder;
    }
    
    private Set<OrderEntry> getOrderEntriesCopy(Set<OrderEntry> entries) {
        return entries.stream().map(oe -> orderEntryDao.getById(oe.getId())).collect(Collectors.toSet());
    }

    @Override
    public Collection<Order> getAll() {
        return orders.stream().map(this::getOrderCopyWithDependencies).collect(Collectors.toSet());
    }

    @Override
    public Order getOriginalDomainObject(Order object) {
        return orders.stream().filter(o -> o.getId() == object.getId()).findFirst().orElse(null);
    }

	@Override
	public Set<Order> getOrdersForUser(User user) {
		orders.stream().filter(o -> o.get
		return null;
	}

}
