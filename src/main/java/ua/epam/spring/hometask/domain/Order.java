package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Order extends DomainObject {
    
//    private Set<OrderEntry> entries = new HashSet<>();
    
    private LocalDateTime dateTime;
    private Long userId;
    
    public Order() {
    }
    
    public Order(Order order) {
        super(order);
    }

//    public Set<OrderEntry> getEntries() {
//        return entries;
//    }
//
//    public void setEntries(Set<OrderEntry> entries) {
//        this.entries = entries;
//    }

    public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
    public int hashCode() {
        return Objects.hash(getId(), dateTime);
    }
    
    @Override
    public boolean equals(Object obj) {
        Order other = (Order) obj;
        return super.equals(obj) && Objects.equals(getId(), other.getId()) && 
        		Objects.equals(userId, other.userId) &&
                Objects.equals(dateTime, other.dateTime);
    }
    
}
