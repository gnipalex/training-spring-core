package ua.epam.spring.hometask.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order extends DomainObject {
    
    private Set<OrderEntry> entries = new HashSet<>();
    
    public Order() {
    }
    
    public Order(Order order) {
        super(order);
    }

    public Set<OrderEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<OrderEntry> entries) {
        this.entries = entries;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(entries);
    }
    
    @Override
    public boolean equals(Object obj) {
        Order other = (Order) obj;
        return super.equals(obj) && Objects.equals(getId(), other.getId()) && 
                Objects.equals(entries, other.getEntries());
    }
    
}
