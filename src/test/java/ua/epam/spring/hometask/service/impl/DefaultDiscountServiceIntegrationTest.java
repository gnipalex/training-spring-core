package ua.epam.spring.hometask.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.DiscountService;
import ua.epam.spring.hometask.service.EventService;
import ua.epam.spring.hometask.service.UserService;

@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DefaultDiscountServiceIntegrationTest extends AbstractServiceIntegrationTest {

    private static final int BASE_EVENT_PRICE = 100;
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime MINUS_FOUR_DAY = NOW.minusDays(4);
    private static final LocalDateTime MINUS_FIVE_DAY = NOW.minusDays(5);
    private static final String ALEX_HNYP_EMAIL_COM = "alex.hnyp@email.com";

    @Resource
    private DiscountService discountService;
    @Resource
    private EventService eventService;
    @Resource
    private UserService userService;
    @Resource
    private AuditoriumService auditoriumService;
    
    private User user;
    private Event event;
    
    @Before
    public void setUp() {
        this.event = eventService.save(prepareEvent());
        this.user = userService.save(prepareUser());
    }
    
    private Event prepareEvent() {
        Event event = new Event();
        
        TreeSet<LocalDateTime> airDates = new TreeSet<>();
        airDates.add(NOW);
        event.setAirDates(airDates);
        event.setBasePrice(BASE_EVENT_PRICE);
        
        TreeMap<LocalDateTime, String> auditoriums = new TreeMap<>();
        auditoriums.put(NOW, "room-imax");
        event.setAuditoriums(auditoriums);
        return event;
    }
    
    private User prepareUser() {
        User user = new User();
        user.setEmail(ALEX_HNYP_EMAIL_COM);
        return user;
    }
    
    @Test
    public void shouldReturnFivePercentDiscount_ifUserHasBirthDayWithinFiveDays() {
        user.setBirthday(MINUS_FOUR_DAY);
        double priceWithDiscount = discountService.getDiscount(user, event, NOW, 1);
        double expectedPrice = (BASE_EVENT_PRICE * 5) / 100;
        assertThat(priceWithDiscount).isEqualByComparingTo(expectedPrice);
    }
    
    @Test
    public void shouldReturnZeroDiscount_ifUserHasBirthDayMoreThanWithinFiveDays() {
        user.setBirthday(MINUS_FIVE_DAY);
        double priceWithDiscount = discountService.getDiscount(user, event, NOW, 1);
        double expectedPrice = 0;
        assertThat(priceWithDiscount).isEqualByComparingTo(expectedPrice);
    }

}
