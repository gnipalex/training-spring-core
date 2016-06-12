package ua.epam.spring.hometask.dao;

import ua.epam.spring.hometask.domain.AbstractDomainObject;

public interface OriginalDomainObjectProvider<T extends AbstractDomainObject> {
    
    T getOriginalDomainObject(T object);
    
}
