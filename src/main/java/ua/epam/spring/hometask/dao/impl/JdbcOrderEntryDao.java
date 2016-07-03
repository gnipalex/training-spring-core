package ua.epam.spring.hometask.dao.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;

@Repository("orderEntryDao")
public class JdbcOrderEntryDao extends AbstractJdbcDao<OrderEntry> implements OrderEntryDao {

    private static final String SELECT_BY_ID = "SELECT * FROM orderEntries WHERE id=?";
    private static final String SELECT_ALL = "SELECT * FROM orderEntries";
    private static final String SELECT_BY_ORDER_ID = "SELECT * FROM orderEntries WHERE orderId=?";
    private static final String INSERT_ORDER_ENTRY = "INSERT INTO orderEntries(basePrice,discount,orderId) VALUES(?,?,?)";
    private static final String UPDATE_ORDER_ENTRY_BY_ID = "UPDATE orderEntries SET basePrice=?, discount=? ,orderId=? WHERE id=?";
    private static final String REMOVE_BY_ORDER_ID = "DELETE FROM orderEntries WHERE orderId=?";
    private static final String REMOVE_BY_ID = "DELETE FROM orderEntries WHERE id=?";
    
    
    @Override
    public OrderEntry save(OrderEntry orderEntry) {
        if (canUpdate(orderEntry)) {
            updateExistingOrderEntry(orderEntry);
        } else {
            saveNewOrderEntry(orderEntry);
        }
        return orderEntry;
    }

    private void saveNewOrderEntry(OrderEntry orderEntry) {
        Object[] args = new Object[] { orderEntry.getBasePrice(), orderEntry.getDiscount(), 
                orderEntry.getOrderId() };
        long generatedId = updateAndGetKey(INSERT_ORDER_ENTRY, args).longValue();
        orderEntry.setId(generatedId);
    }

    private void updateExistingOrderEntry(OrderEntry orderEntry) {
        Object[] args = new Object[] { orderEntry.getBasePrice(), orderEntry.getDiscount(), 
                orderEntry.getOrderId(), orderEntry.getId() };
        updateRow(UPDATE_ORDER_ENTRY_BY_ID, args);
    }

    private boolean canUpdate(OrderEntry orderEntry) {
        return getById(orderEntry.getId()) != null;
    }

    @Override
    public void remove(OrderEntry orderEntry) {
        getJdbcTemplate().update(REMOVE_BY_ID, orderEntry.getId());
    }

    @Override
    public OrderEntry getById(long id) {
        return queryForObject(SELECT_BY_ID, id);
    }

    @Override
    public Collection<OrderEntry> getAll() {
        return queryForList(SELECT_ALL);
    }

    @Override
    public void removeOrderEntriesForOrder(Order order) {
        getJdbcTemplate().update(REMOVE_BY_ORDER_ID, order.getId());
    }

    @Override
    public Set<OrderEntry> getOrderEntriesForOrder(Order order) {
        List<OrderEntry> orderEntries = queryForList(SELECT_BY_ORDER_ID, order.getId());
        return orderEntries.stream().collect(Collectors.toSet());
    }

    @Override
    protected RowMapper<OrderEntry> getRowMapper() {
        return (resultSet, rowNumber) -> {
            OrderEntry orderEntry = new OrderEntry();
            orderEntry.setBasePrice(resultSet.getDouble("basePrice"));
            orderEntry.setDiscount(resultSet.getDouble("discount"));
            orderEntry.setOrderId(resultSet.getLong("orderId"));
            orderEntry.setId(resultSet.getLong("id"));
            return orderEntry;
            
        };
    }

}
