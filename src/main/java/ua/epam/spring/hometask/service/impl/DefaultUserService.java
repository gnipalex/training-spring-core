package ua.epam.spring.hometask.service.impl;

import java.util.Collection;
import java.util.Objects;

import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.UserService;

public class DefaultUserService implements UserService {

    private UserDao userDao;
	
	@Override
    public User save(User user) {
		Objects.requireNonNull(user);
        return userDao.save(user);
    }

    @Override
    public void remove(User user) {
    	Objects.requireNonNull(user);
    	userDao.remove(user);
    }

    @Override
    public User getById(long id) {
        return userDao.getById(id);
    }

    @Override
    public Collection<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User getUserByEmail(String email) {
    	Objects.requireNonNull(email);
        return userDao.getUserByEmail(email);
    }

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
