package ua.epam.spring.hometask.aspect;

import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import ua.epam.spring.hometask.dao.OrderDao;
import ua.epam.spring.hometask.dao.OrderEntryDao;
import ua.epam.spring.hometask.domain.Order;
import ua.epam.spring.hometask.domain.OrderEntry;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.strategy.LuckyUserStrategy;

@Aspect
public class LuckyWinnerAspect {

    private static final String NEW_LINE = "\n";

    private static final String YOU_ARE_LUCKY_MESSAGE = "You are lucky, thats why you get your tickets for free. Enjoy!!";

    private static final Logger LOG = Logger.getLogger(LuckyWinnerAspect.class);
    
    @Autowired(required = false)
    private LuckyUserStrategy luckyUserStrategy;
    @Resource
    private OrderEntryDao orderEntryDao;
    @Resource
    private OrderDao orderDao;
    
    @Around(value = "execution(* ua.epam.spring.hometask.service.BookingService.bookTickets(..)) && args(user,..)")
    public Order onBookTickets(ProceedingJoinPoint proceedingJoinPoint, User user) throws Throwable {
        Order actualOrder = (Order) proceedingJoinPoint.proceed();
        if (checkLucky(user)) {
            resetPriceInAllOrderEntries(actualOrder);
            addLuckyDescriptionToOrder(actualOrder);
            LOG.info("Congratulations!! You are lucky!! Your tickets are for free!!");
        }
        return actualOrder;
    }
    
    private void addLuckyDescriptionToOrder(Order actualOrder) {
        String description = actualOrder.getDescription();
        if (StringUtils.isNoneBlank(description)) {
            description += NEW_LINE;
        } 
        description += YOU_ARE_LUCKY_MESSAGE;
        actualOrder.setDescription(description);
        orderDao.save(actualOrder);
    }

    private void resetPriceInAllOrderEntries(Order actualOrder) {
        Set<OrderEntry> orderEntries = orderEntryDao.getOrderEntriesForOrder(actualOrder);
        orderEntries.forEach(entry -> {
            entry.setBasePrice(0D);
            entry.setDiscount(0D);
            orderEntryDao.save(entry);
        });
    }
    
    private boolean checkLucky(User user) {
        if (Objects.nonNull(luckyUserStrategy)) {
            return luckyUserStrategy.checkLucky(user);
        }
        return false;
    }
    
}
