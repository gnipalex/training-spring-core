package ua.epam.spring.hometask.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.service.AuditoriumService;
import ua.epam.spring.hometask.service.EventService;

//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class DefaultEventServiceIntegrationTest extends AbstractServiceIntegrationTest {

    private static final String EVENT_1_NAME = "event 1 name";
    private static final String EVENT_2_NAME = "event 2 name";
    private static final double EVENT_1_BASE_PRICE = 24D;
    private static final LocalDateTime EVENT_1_DATE  = LocalDateTime.now();
    private static final String EVENT_1_AUDITORIUM_CODE = "test-auditorium-1";
    private static final LocalDateTime EVENT_2_DATE = EVENT_1_DATE.minusHours(5);
    
    @Resource
    private EventService eventService;
    @Resource
    private AuditoriumService auditoriumService;
    
    private Event event1;
    
    @Before
    public void before() {
        Event event = prepareEvent(EVENT_1_NAME);
        event1 = eventService.save(event);
    }

    private Event prepareEvent(String name) {
        Event event = new Event();
        event.addAirDateTime(EVENT_1_DATE);
        Auditorium auditorium1 = auditoriumService.getByCode(EVENT_1_AUDITORIUM_CODE);
        event.assignAuditorium(EVENT_1_DATE, auditorium1);
        event.setBasePrice(EVENT_1_BASE_PRICE);
        event.setName(name);
        event.setRating(EventRating.HIGH);
        return event;
    }
    
    @Test
    public void shouldReturnEventByName() {
        Event event = eventService.getByName(EVENT_1_NAME);
        assertThat(event).isEqualTo(event1);
    }
    
    @Test
    public void shouldSaveEvent() {
        assertThat(eventService.getAll()).hasSize(1);
        Event event = prepareEvent(EVENT_2_NAME);
        Event savedEvent = eventService.save(event);
        assertThat(savedEvent).isNotNull();
        assertThat(eventService.getAll()).hasSize(2);
    }
    
    @Test
    public void shouldSaveAllEventFields() {
        Event event = eventService.getByName(EVENT_1_NAME);
        assertThat(event.getAirDates()).isEqualTo(event1.getAirDates());
        assertThat(event.getAuditoriums()).isEqualTo(event1.getAuditoriums());
        assertThat(event.getBasePrice()).isEqualTo(event1.getBasePrice());
        assertThat(event.getId()).isEqualTo(event1.getId());
        assertThat(event.getName()).isEqualTo(event1.getName());
        assertThat(event.getRating()).isEqualTo(event.getRating());
    }
    
    @Test(expected=RuntimeException.class)
    public void shouldThrowRuntimeException_whenSaveEventWithExistingName() {
        Event event = prepareEvent(EVENT_1_NAME);
        eventService.save(event);
    }

}
