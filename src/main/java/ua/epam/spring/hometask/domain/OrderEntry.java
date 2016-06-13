package ua.epam.spring.hometask.domain;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class OrderEntry extends DomainObject {

    private Set<Ticket> tickets = new HashSet<>();
    private double basePrice;
    private double discount;
    
    public OrderEntry() {
    }

    public OrderEntry(OrderEntry orderEntry) {
        super(orderEntry);
        this.basePrice = orderEntry.getBasePrice();
        this.discount = orderEntry.discount;
    }
    
    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(tickets, basePrice, discount);
    }
    
    @Override
    public boolean equals(Object obj) {
        OrderEntry other = (OrderEntry) obj;
        return super.equals(other) && Objects.equals(tickets, other.tickets) && 
                Objects.equals(basePrice, other.basePrice) && 
                Objects.equals(discount, other.discount);
    }

}
