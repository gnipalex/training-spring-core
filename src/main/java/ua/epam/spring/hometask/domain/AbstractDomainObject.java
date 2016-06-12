package ua.epam.spring.hometask.domain;

import java.util.Objects;

public abstract class AbstractDomainObject {

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return Objects.nonNull(obj) && Objects.equals(getClass(), obj.getClass());
    }
    
}
