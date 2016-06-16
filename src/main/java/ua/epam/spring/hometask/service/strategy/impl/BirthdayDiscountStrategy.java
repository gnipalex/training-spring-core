package ua.epam.spring.hometask.service.strategy.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

public class BirthdayDiscountStrategy implements DiscountStrategy {

    private int withinDays;
    private byte discountValue;

    @Override
    public double getDiscount(User user, Event event,
            LocalDateTime airDateTime, long numberOfTickets) {
        if (isDiscountApplicable(user)) {
            double initialPrice = getInitialPrice(event, numberOfTickets);
            return (initialPrice * discountValue) / 100;
        }
        return 0;
    }

    private double getInitialPrice(Event event, long numberOfTickets) {
        return event.getBasePrice() * numberOfTickets;
    }

    private boolean isDiscountApplicable(User user) {
        if (user != null) {
            return isUserBirthdayWithinDiscountDays(user.getBirthday());
        }
        return false;
    }

    private boolean isUserBirthdayWithinDiscountDays(LocalDateTime userBirthday) {
        if (userBirthday != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            long daysBetweenDates = ChronoUnit.DAYS.between(userBirthday,
                    currentTime);
            return Math.abs(daysBetweenDates) < withinDays;
        }
        return false;
    }

    public void setDiscountValue(byte discountValue) {
        this.discountValue = discountValue;
    }

    public void setWithinDays(int withinDays) {
        this.withinDays = withinDays;
    }

}
