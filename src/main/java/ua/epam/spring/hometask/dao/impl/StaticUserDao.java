package ua.epam.spring.hometask.dao.impl;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.OriginalDomainObjectProvider;
import ua.epam.spring.hometask.dao.TicketDao;
import ua.epam.spring.hometask.dao.UserDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

public class StaticUserDao implements UserDao, OriginalDomainObjectProvider<User> {

    private Set<User> users = new HashSet<>();
    
    private OriginalDomainObjectProvider<Ticket> originalTicketObjectProvider;
    private OriginalDomainObjectProvider<Order> originalOrderObjectProvider;
    private TicketDao ticketDao;
    private IdGenerator userIdGenerator;
    
    @Override
    public User getUserByEmail(String email) {
        User originalUser = getOriginalUserBy(u -> Objects.equals(u.getEmail(), email));
        return Optional.ofNullable(originalUser).map(this::getUserCopyWithDependencies).orElse(null);
    }
    
    private User getUserCopyWithDependencies(User user) {
        User copyUser = new User(user);
        setTickets(copyUser);
        return copyUser;
    }

    private void setTickets(User copyUser) {
        Set<Ticket> tickets = ticketDao.getTicketsForUser(copyUser);
        if (tickets != null) {
            copyUser.setTickets(new TreeSet<>(tickets));
        }
    }

    @Override
    public User save(User newUser) {
        User saveUser = null;
        if (canUpdate(newUser)) {
            saveUser = updateExistingUser(newUser);
        } else {
            saveUser = createNewUser(newUser);
        }
        return getUserCopyWithDependencies(saveUser);
    }

    private User createNewUser(User newUser) {
        User saveUser;
        saveUser = new User(newUser);
        saveUser.setId(userIdGenerator.generateNextId());
        users.add(saveUser);
        return saveUser;
    }

    private User updateExistingUser(User newUser) {
        User saveUser;
        saveUser = getOriginalDomainObject(newUser);
        saveUser.setBirthday(newUser.getBirthday());
        saveUser.setEmail(newUser.getEmail());
        saveUser.setFirstName(newUser.getFirstName());
        saveUser.setLastName(newUser.getLastName());
        return saveUser;
    }
    
    private boolean canUpdate(User newUser) {
        User originalUser = getOriginalDomainObject(newUser);
        boolean userAlreadyExists = users.contains(newUser);
        if (originalUser == null && userAlreadyExists) {
            throw new IllegalStateException("user already exists");
        }
        return originalUser != null;
    }

    @Override
    public void remove(User user) {
        User originalUser = getOriginalDomainObject(user);
        if (originalUser == null) {
            throw new IllegalStateException("user does not exist");
        }
//        removeUserFromTickets(user);
        users.remove(originalUser);
    }
    
    private void removeUserFromOrders(User user) {
    	originalOrderObjectProvider.g
    }

//    private void removeUserFromTickets(User user) {
//        Set<Ticket> userTickets = ticketDao.getTicketsForUser(user);
//        if (userTickets != null) {
//            userTickets.stream().map(originalTicketObjectProvider::getOriginalDomainObject)
//                .forEach(originalTicket -> {
//                    originalTicket.setUser(null);
//                });
//        }
//    }

    @Override
    public User getById(long id) {
        return users.stream().filter(u -> u.getId() == id).map(this::getUserCopyWithDependencies).findFirst().orElse(null);
    }

    @Override
    public Collection<User> getAll() {
        return users.stream().map(this::getUserCopyWithDependencies).collect(Collectors.toList());
    }
    
    private User getOriginalUserBy(Predicate<User> predicate) {
        return users.stream().filter(predicate).findFirst().orElse(null);
    }

    @Override
    public User getOriginalDomainObject(User object) {
        return getOriginalUserBy(u -> u.getId() == object.getId());
    }

}
