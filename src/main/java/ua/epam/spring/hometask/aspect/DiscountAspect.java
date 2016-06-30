package ua.epam.spring.hometask.aspect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.DiscountStrategy;

@Aspect
public class DiscountAspect {

    private static final Logger LOG = Logger.getLogger(DiscountAspect.class);
    
    private Map<String, Long> discountAppliedTimes = new HashMap<>();
    private Map<String, Map<String, Long>> discountAppliedTimesPerUser = new HashMap<>();
    
    private ThreadLocal<Boolean> bookTicketsMethodWasEnteredFlag = new ThreadLocal<>();
    private ThreadLocal<User> currentUser = new ThreadLocal<>();
    private ThreadLocal<DiscountApplyInfo> currentDiscountApplyInfo = new ThreadLocal<>();
    
    
    @Pointcut("execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..))")
    private void bookTickets() {}
    
    @Around("bookTickets() && args(user,..)")
    public Object aroundBookTickets(ProceedingJoinPoint proceedingJoinPoint, User user) throws Throwable {
        setBookTicketsMethodWasEnteredFlag(true);
        setCurrentUser(user);
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            setBookTicketsMethodWasEnteredFlag(false);  
            setCurrentUser(null);
        }
    }

    @Pointcut("execution(* ua.epam.spring.hometask.service.DiscountService.getDiscount(..))")
    private void discountServiceGetDiscount() {}
        
    @After("discountServiceGetDiscount()")
    public void afterDiscountReturned(JoinPoint joinPoint) {
        if (wasAnyDiscountApplied()) {
            if (currentUserExists()) {
                saveDiscountApplyInfoForUser();
            } 
            saveTotalDiscountApplyInfo();
        }
        resetCurrentDiscountApplyInfo();
    }
    
    private boolean wasAnyDiscountApplied() {
        return getCurrentDiscountApplyInfo().value > 0;
    }
    
    private void saveTotalDiscountApplyInfo() {
        DiscountApplyInfo discountInfo = getCurrentDiscountApplyInfo();
        String discountClassName = discountInfo.discountClass.getSimpleName();
        
        long incrementedDiscountApplyTimes = increment(discountAppliedTimes.get(discountClassName));
        discountAppliedTimes.put(discountClassName, incrementedDiscountApplyTimes);
        
        LOG.info(String.format("discount [%s] was applied [%d] times in total", discountClassName, incrementedDiscountApplyTimes));
    }
    
    private long increment(Long value) {
        long valueToIncrement = Objects.nonNull(value) ? value.longValue() : 0L;
        return ++valueToIncrement;
    }

    private void saveDiscountApplyInfoForUser() {
        DiscountApplyInfo discountInfo = getCurrentDiscountApplyInfo();
        User user = getCurrentUser();
        String discountClassName = discountInfo.discountClass.getSimpleName();
        
        long incrementedDiscountApplyTimes = increment(getDiscountApplyTimesForDiscountNameAndUser(discountClassName, user));
        setDiscountApplyTimesForDiscountNameAndUser(discountClassName, user, incrementedDiscountApplyTimes);
        
        LOG.info(String.format("discount [%s] was applied [%d] times for user [%s]", discountClassName, incrementedDiscountApplyTimes,user.getEmail()));
    }
    
    private Long getDiscountApplyTimesForDiscountNameAndUser(String discountName, User user) {
        Map<String, Long> discountsApplyPerUser = discountAppliedTimesPerUser.getOrDefault(discountName, Collections.emptyMap());
        return discountsApplyPerUser.get(user.getEmail());
    }
    
    private void setDiscountApplyTimesForDiscountNameAndUser(String discountName, User user, long discountApplyTimes) {
        Map<String, Long> discountsApplyPerUser = discountAppliedTimesPerUser.getOrDefault(discountName, new HashMap<>());
        discountsApplyPerUser.put(user.getEmail(), discountApplyTimes);
        discountAppliedTimesPerUser.put(discountName, discountsApplyPerUser);
    }

    private boolean currentUserExists() {
        return Objects.nonNull(getCurrentUser());
    }
    
    @Pointcut("execution(* ua.epam.spring.hometask.service.strategy.DiscountStrategy.getDiscount(..))")
    private void discountStrategyApply() {}
    
    @AfterReturning(value = "discountStrategyApply()", returning = "discountValue" )
    @SuppressWarnings("unchecked")
    public void afterDiscountStrategyApply(JoinPoint joinPoint, double discountValue) {
        if (getBookTicketsMethodWasEnteredFlag()) {
            Class<? extends DiscountStrategy> discountStrategyClass = (Class<? extends DiscountStrategy>) joinPoint.getTarget().getClass();
            updateCurrentDiscountApplyInfo(discountStrategyClass, discountValue);
        }
    }
    
    private void updateCurrentDiscountApplyInfo(Class<? extends DiscountStrategy> discountStrategyClass, double discountValue) {
        DiscountApplyInfo discountApplyInfo = getCurrentDiscountApplyInfo();
        boolean discountHasBiggerValue = discountValue > discountApplyInfo.value;
        if (discountHasBiggerValue) {
            discountApplyInfo.discountClass = discountStrategyClass;
            discountApplyInfo.value = discountValue;
        }
    }
    
    private boolean getBookTicketsMethodWasEnteredFlag() {
        return Boolean.TRUE.equals(bookTicketsMethodWasEnteredFlag.get());
    }
    
    private void setBookTicketsMethodWasEnteredFlag(boolean value) {
        bookTicketsMethodWasEnteredFlag.set(value);
    }
    
    private void resetCurrentDiscountApplyInfo() {
        currentDiscountApplyInfo.set(new DiscountApplyInfo());
    }
    
    private DiscountApplyInfo getCurrentDiscountApplyInfo() {
        DiscountApplyInfo discountApplyInfo = currentDiscountApplyInfo.get();
        if (Objects.isNull(discountApplyInfo)) {
            resetCurrentDiscountApplyInfo();
            discountApplyInfo = currentDiscountApplyInfo.get();
        }
        return discountApplyInfo;
    }
    
    private User getCurrentUser() {
        return currentUser.get();
    }
    
    private void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    private class DiscountApplyInfo {
        private double value;
        private Class<? extends DiscountStrategy> discountClass;
    }
    
}
