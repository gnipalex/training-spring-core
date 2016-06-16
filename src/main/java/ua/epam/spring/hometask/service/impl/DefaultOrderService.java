package ua.epam.spring.hometask.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.OrderService;

public class DefaultOrderService implements OrderService {

	private OrderDao orderDao;
	private OrderEntryDao orderEntryDao;
	
	@Override
	public Order createOrder(User user, List<OrderEntry> orderEntries) {
		Order savedOrder = createEmptyOrder(user);
		updateOrderEntries(orderEntries, savedOrder);
		orderEntries.forEach(orderEntryDao::save);
		return savedOrder;
	}

	private void updateOrderEntries(List<OrderEntry> orderEntries, Order savedOrder) {
		orderEntries.stream().forEach(orderEntry -> orderEntry.setOrderId(savedOrder.getId()));
		orderEntries.forEach(orderEntryDao::save);
	}

	private Order createEmptyOrder(User user) {
		Order createdOrder = new Order();
		if (user != null) {
			createdOrder.setUserId(user.getId());
		}
		createdOrder.setDateTime(LocalDateTime.now());
		return orderDao.save(createdOrder);
	}

	@Override
	public Order getOrder(long id) {
		return orderDao.getById(id);
	}

	@Override
	public Collection<Order> getUserOrders(User user) {
		return orderDao.getOrdersForUser(user);
	}

	@Override
	public double getOrderPrice(Order order) {
		Set<OrderEntry> orderEntries = orderEntryDao.getOrderEntriesForOrder(order);
		return orderEntries.stream().mapToDouble(this::getPriceWithDiscount).sum();
	}
	
	private double getPriceWithDiscount(OrderEntry orderEntry) {
		return orderEntry.getBasePrice() - orderEntry.getDiscount();
	}

	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setOrderEntryDao(OrderEntryDao orderEntryDao) {
		this.orderEntryDao = orderEntryDao;
	}

}
