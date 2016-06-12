package ua.epam.spring.hometask.dao;

import java.util.Collection;

import javax.annotation.Nonnull;

import ua.epam.spring.hometask.domain.DomainObject;

public interface DomainObjectDao <T extends DomainObject> {
    
    T save(T object);

    void remove(T object);

    T getById(@Nonnull long id);

    Collection<T> getAll();
    
}
