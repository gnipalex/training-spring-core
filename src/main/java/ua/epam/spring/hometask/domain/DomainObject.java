package ua.epam.spring.hometask.domain;


/**
 * @author Yuriy_Tkach
 */
public class DomainObject extends AbstractDomainObject {

    private long id;
    
    public DomainObject() {
    }
    
    public DomainObject(DomainObject domainObject) {
        this.id = domainObject.id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
