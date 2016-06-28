package ua.epam.spring.hometask.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ua.epam.spring.hometask.dao.IdGenerator;
import ua.epam.spring.hometask.dao.SimpleIdGenerator;

@Configuration
@ComponentScan(basePackages = {"ua.epam.spring.hometask.dao.impl"})
public class DaoContext {
    
    @Bean
    @Scope(value = "prototype")
    public IdGenerator idGenerator() {
        return new SimpleIdGenerator();
    }
    
//    @Bean
//    public OrderDao orderDao(IdGenerator idGenerator, OrderEntryDao orderEntryDao) {
//        InMemoryOrderDao inMemoryOrderDao = new InMemoryOrderDao();
//        inMemoryOrderDao.setIdGenerator(idGenerator);
//        inMemoryOrderDao.setOrderEntryDao(orderEntryDao);
//        return inMemoryOrderDao;
//    }
//    
//    @Bean
//    public EventDao eventDao(IdGenerator idGenerator, TicketDao ticketDao, 
//            AuditoriumDao auditoriumDao) {
//        InMemoryEventDao inMemoryEventDao = new InMemoryEventDao();
//        inMemoryEventDao.setIdGenerator(idGenerator);
//        inMemoryEventDao.setAuditoriumDao(auditoriumDao);
//        inMemoryEventDao.setTicketDao(ticketDao);
//        return inMemoryEventDao;
//    }
//    
//    @Bean
//    public OrderEntryDao orderEntryDao(IdGenerator idGenerator,
//            TicketDao ticketDao) {
//        InMemoryOrderEntryDao inMemoryOrderEntryDao = new InMemoryOrderEntryDao();
//        inMemoryOrderEntryDao.setIdGenerator(idGenerator);
//        inMemoryOrderEntryDao.setTicketDao(ticketDao);
//        return inMemoryOrderEntryDao;
//    }
//    
//    @Bean
//    public TicketDao ticketDao(IdGenerator idGenerator, 
//            OrderDao orderDao, 
//            OrderEntryDao orderEntryDao) {
//        InMemoryTicketDao inMemoryTicketDao = new InMemoryTicketDao();
//        inMemoryTicketDao.setIdGenerator(idGenerator);
//        inMemoryTicketDao.setOrderDao(orderDao);
//        inMemoryTicketDao.setOrderEntryDao(orderEntryDao);
//        return inMemoryTicketDao;
//    }
//    
//    @Bean
//    public UserDao userDao(IdGenerator idGenerator) {
//        InMemoryUserDao inMemoryUserDao = new InMemoryUserDao();
//        inMemoryUserDao.setIdGenerator(idGenerator);
//        return inMemoryUserDao;
//    }

}
