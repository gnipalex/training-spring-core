package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yuriy_Tkach
 */
public class Ticket extends DomainObject { 

    private LocalDateTime dateTime;
    private long seat;
    private long orderEntryId;
    private long eventId;
    
    public Ticket() {
    }
    
    public Ticket(Ticket ticket) {
        super(ticket);
        this.dateTime = ticket.dateTime;
        this.seat = ticket.seat;
        this.eventId = ticket.eventId;
        this.orderEntryId = ticket.orderEntryId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public long getSeat() {
        return seat;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
		this.eventId = eventId;
	}

	public void setSeat(long seat) {
        this.seat = seat;
    }
	
    public long getOrderEntryId() {
		return orderEntryId;
	}

	public void setOrderEntryId(long orderEntryId) {
		this.orderEntryId = orderEntryId;
	}

	@Override
    public int hashCode() {
        return Objects.hash(dateTime, eventId, seat);
    }

    @Override
    public boolean equals(Object obj) {
        Ticket other = (Ticket) obj;
        return super.equals(other) && 
        		Objects.equals(dateTime, other.dateTime) &&
        		Objects.equals(seat, other.seat) &&
                Objects.equals(eventId, other.eventId);
    }

}
