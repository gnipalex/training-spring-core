package ua.epam.spring.hometask.service.strategy.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
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
        if (isDiscountApplicable(user, airDateTime)) {
            double initialPrice = getInitialPrice(event, numberOfTickets);
            return (initialPrice * discountValue) / 100;
        }
        return 0;
    }

    private double getInitialPrice(Event event, long numberOfTickets) {
        return event.getBasePrice() * numberOfTickets;
    }

    private boolean isDiscountApplicable(User user, LocalDateTime airDateTime) {
        if (user != null) {
            return isUserBirthdayWithinDiscountDays(user.getBirthday(), airDateTime);
        }
        return false;
    }

    private boolean isUserBirthdayWithinDiscountDays(LocalDateTime userBirthday, LocalDateTime airDateTime) {
        if (userBirthday != null) {
            
            long daysBetweenDates = ChronoUnit.DAYS.between(resetYearAndGetDate(userBirthday), 
                    resetYearAndGetDate(airDateTime));
            return Math.abs(daysBetweenDates) < withinDays;
        }
        return false;
    }
    
    private LocalDate resetYearAndGetDate(LocalDateTime dateTime) {
        return dateTime.withYear(0).toLocalDate();
    }

    public void setDiscountValue(byte discountValue) {
        this.discountValue = discountValue;
    }

    public void setWithinDays(int withinDays) {
        this.withinDays = withinDays;
    }
    
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.of(2016, 3, 4, 5, 6);
        System.out.println(dateTime);
        System.out.println(dateTime.withYear(0));
        MonthDay eventMonthDay = MonthDay.from(dateTime);
        System.out.println(eventMonthDay);
    }

}
