package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order extends DomainObject {
    
    private LocalDateTime dateTime;
    private Long userId;
    private String description;
    
    public Order() {
    }
    
    public Order(Order order) {
        super(order);
        this.dateTime = order.getDateTime();
        this.userId = order.userId;
        this.description = order.description;
    }

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
	
	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
