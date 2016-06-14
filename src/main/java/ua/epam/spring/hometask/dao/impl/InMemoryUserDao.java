package ua.epam.spring.hometask.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.domain.User;

public class InMemoryUserDao implements UserDao  {

	private List<User> users = new ArrayList<>();
	
	private IdGenerator idGenerator;

	@Override
	public User save(User object) {
		User savedUser = null;
		if (userExists(object)) {
			savedUser = getOriginalUserById(object.getId()).get();
		} else {
			savedUser = new User();
			savedUser.setId(idGenerator.generateNextId());
			users.add(savedUser);
		}
		savedUser.setBalance(object.getBalance());
		savedUser.setBirthday(object.getBirthday());
		savedUser.setEmail(object.getEmail());
		savedUser.setFirstName(object.getFirstName());
		savedUser.setLastName(object.getLastName());
		return getCopy(savedUser);
	}
	
	private User getCopy(User user) {
		return new User(user);
	}

	private boolean userExists(User object) {
		return users.stream().anyMatch(u -> u.getId() == object.getId());
	}

	@Override
	public void remove(User object) {
		if (!userExists(object)) {
			throw new IllegalArgumentException("user does not exist");
		}
		users.remove(object);
	}

	@Override
	public User getById(long id) {
		return getOriginalUserById(id).map(User::new).orElse(null);
	}
	
	private Optional<User> getOriginalUserById(long id) {
		return users.stream().filter(u -> u.getId() == id).findFirst();
	}

	@Override
	public Collection<User> getAll() {
		return users.stream().map(this::getCopy).collect(Collectors.toList());
	}

	@Override
	public User getUserByEmail(String email) {
		return users.stream().filter(u -> Objects.equals(u.getEmail(), email)).findFirst().map(this::getCopy).orElse(null);
	}
	
}
