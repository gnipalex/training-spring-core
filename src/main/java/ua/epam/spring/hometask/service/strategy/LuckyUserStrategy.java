package ua.epam.spring.hometask.service.strategy;

import ua.epam.spring.hometask.domain.User;

public interface LuckyUserStrategy {

    boolean checkLucky(User user);
    
}
