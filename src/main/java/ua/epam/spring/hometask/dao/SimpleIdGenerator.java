package ua.epam.spring.hometask.dao;

public class SimpleIdGenerator implements IdGenerator {

    private long previouslyGeneratedId = 0L;
    
    @Override
    public long generateNextId() {
        return ++previouslyGeneratedId;
    }

}
