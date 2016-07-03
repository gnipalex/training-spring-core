package ua.epam.spring.hometask.dao.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.User;

@Repository("orderDao")
public class JdbcOrderDao extends AbstractJdbcDao<Order> implements OrderDao {

    private static final String INSERT_ORDER = "INSERT INTO orders(dateTime,userId,description) VALUES(?,?,?)";
    private static final String UPDATE_ORDER_BY_ID = "UPDATE orders SET dateTime=?, userId=?, description=? WHERE id=?";
    private static final String REMOVE_BY_ID = "DELETE FROM orders WHERE id=?";
    private static final String SELECT_BY_ID = "SELECT * FROM orders WHERE id=?";
    private static final String SELECT_ALL = "SELECT * FROM orders";
    private static final String SELECT_BY_USER_ID = "SELECT * FROM orders WHERE userId=?";
    
    @Override
    public Order save(Order order) {
        if (canUpdate(order)) {
            updateExistingOrder(order);
        } else {
            saveNewOrder(order);
        }
        return order;
    }

    private void updateExistingOrder(Order order) {
        Object[] args = new Object[] { toTimestamp(order.getDateTime()), order.getUserId(), 
                order.getDescription(), order.getId() };
        updateRow(UPDATE_ORDER_BY_ID, args);
    }

    private void saveNewOrder(Order order) {
        Object[] args = new Object[] { toTimestamp(order.getDateTime()), order.getUserId(),
                order.getDescription() };
        long generatedId = updateAndGetKey(INSERT_ORDER, args).longValue();
        order.setId(generatedId);
    }

    private boolean canUpdate(Order order) {
        return getById(order.getId()) != null;
    }

    @Override
    public void remove(Order order) {
        getJdbcTemplate().update(REMOVE_BY_ID, order.getId());
    }

    @Override
    public Order getById(long id) {
        return queryForObject(SELECT_BY_ID, id);
    }

    @Override
    public Collection<Order> getAll() {
        return queryForList(SELECT_ALL);
    }

    @Override
    public Collection<Order> getOrdersForUser(User user) {
        return queryForList(SELECT_BY_USER_ID, user.getId());
    }

    @Override
    protected RowMapper<Order> getRowMapper() {
        return (resultSet, rowNumber) -> {
            Order order = new Order();
            order.setDateTime( toLocalDateTime(resultSet.getTimestamp("dateTime")) );
            order.setDescription(resultSet.getString("description"));
            order.setId(resultSet.getLong("id"));
            order.setUserId(resultSet.getLong("userId"));
            return order;
        };
    }
    
    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
    
    private Timestamp toTimestamp(LocalDateTime localDateTime) {
        return localDateTime != null ? Timestamp.valueOf(localDateTime) : null;
    }

}
