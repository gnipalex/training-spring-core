package ua.epam.spring.hometask.dao.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.dao.util.DaoUtils;
import ua.epam.spring.hometask.domain.User;

@Repository("userDao")
public class JdbcUserDao extends AbstractJdbcDao<User> implements UserDao {

    private static final String SELECT_BY_ID = "SELECT * FROM users WHERE id=?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM users WHERE email=?";
    private static final String SELECT_ALL = "SELECT * FROM users";
    private static final String INSERT_USER = "INSERT INTO users(birthday,firstName,lastName,email) VALUES(?,?,?,?)";
    private static final String UPDATE_USER = "UPDATE users SET birthday=?, firstName=?, lastName=?, email=? WHERE id=?";
    private static final String REMOVE_BY_ID = "DELETE FROM users WHERE id=?";
    
    @Override
    public User save(User user) {
        if(canUpdate(user)) {
            updateExistingUser(user);
        } else {
            saveNewUser(user);
        }
        return user;
    }

    private void saveNewUser(User user) {
        Object[] args = new Object[] { DaoUtils.toTimestamp(user.getBirthday()), 
            user.getFirstName(), user.getLastName(), user.getEmail() };
        long generatedId = updateAndGetKey(INSERT_USER, args).longValue();
        user.setId(generatedId);
    }

    private void updateExistingUser(User user) {
        Object[] args = new Object[] { DaoUtils.toTimestamp(user.getBirthday()), 
                user.getFirstName(), user.getLastName(), user.getEmail(),
                user.getId() };
        updateRow(UPDATE_USER, args);
    }

    private boolean canUpdate(User user) {
        return getById(user.getId()) != null;
    }

    @Override
    public void remove(User user) {
        getJdbcTemplate().update(REMOVE_BY_ID, user.getId());
    }

    @Override
    public User getById(long id) {
        return queryForObject(SELECT_BY_ID, id);
    }

    @Override
    public Collection<User> getAll() {
        List<User> users = queryForList(SELECT_ALL);
        return DaoUtils.toSet(users);
    }

    @Override
    public User getUserByEmail(String email) {
        return queryForObject(SELECT_BY_EMAIL, email);
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return (resultSet, rowNumber) -> {
            User user = new User();
            user.setBirthday(DaoUtils.toLocalDateTime(resultSet.getTimestamp("birthday")));
            user.setEmail(resultSet.getString("email"));
            user.setFirstName(resultSet.getString("firstName"));
            user.setId(resultSet.getLong("id"));
            user.setLastName(resultSet.getString("lastName"));
            return user;
        };
    }

}
