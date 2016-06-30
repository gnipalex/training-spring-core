package ua.epam.spring.hometask.service.strategy.impl;

import java.util.Objects;
import java.util.Random;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.LuckyUserStrategy;

public class RandomLuckyUserStrategy implements LuckyUserStrategy {

    private Random random = new Random();
    
    @Override
    public boolean checkLucky(User user) {
        return Objects.nonNull(user) && random.nextBoolean();
    }

}
