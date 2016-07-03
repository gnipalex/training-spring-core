package ua.epam.spring.hometask.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.User;

//@Repository("orderDao")
public class InMemoryOrderDao implements OrderDao {

    private List<Order> orders = new ArrayList<>();

    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private IdGenerator idGenerator;

    @Override
    public Order save(Order object) {
        Order savedOrder = null;
        if (orderExists(object)) {
            savedOrder = getOriginalOrderById(object.getId()).get();
        } else {
            savedOrder = new Order();
            savedOrder.setId(idGenerator.generateNextId());
            orders.add(savedOrder);
        }
        savedOrder.setDateTime(object.getDateTime());
        savedOrder.setUserId(object.getUserId());
        savedOrder.setDescription(object.getDescription());
        return getOrderCopy(savedOrder);
    }

    private boolean orderExists(Order object) {
        return getOriginalOrderById(object.getId()).isPresent();
    }

    @Override
    public void remove(Order object) {
        if (!orderExists(object)) {
            throw new IllegalArgumentException("order does not exist");
        }
        orderEntryDao.removeOrderEntriesForOrder(object);
        orders.remove(object);
    }

    @Override
    public Order getById(long id) {
        return getOriginalOrderById(id).orElse(null);
    }

    private Optional<Order> getOriginalOrderById(long id) {
        return orders.stream().filter(o -> o.getId() == id).findFirst();
    }

    @Override
    public Collection<Order> getAll() {
        return orders.stream().map(this::getOrderCopy)
                .collect(Collectors.toList());
    }

    private Order getOrderCopy(Order order) {
        return new Order(order);
    }

    @Override
    public Collection<Order> getOrdersForUser(User user) {
        return orders.stream()
                .filter(o -> Objects.equals(o.getUserId(), user.getId()))
                .map(this::getOrderCopy).collect(Collectors.toSet());
    }

    public void setOrderEntryDao(OrderEntryDao orderEntryDao) {
        this.orderEntryDao = orderEntryDao;
    }

    public void setIdGenerator(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

}
