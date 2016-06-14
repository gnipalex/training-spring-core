package ua.epam.spring.hometask.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Yuriy_Tkach
 */
public class Ticket extends DomainObject { //implements Comparable<Ticket> {

    private LocalDateTime dateTime;
    private long seat;
//    private long userId;
    private long orderEntryId;
    private long eventId;
    
    public Ticket() {
    }
    
    public Ticket(Ticket ticket) {
        super(ticket);
        this.dateTime = ticket.dateTime;
        this.seat = ticket.seat;
        this.eventId = ticket.eventId;
//        this.userId = ticket.userId;
        this.orderEntryId = ticket.orderEntryId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public long getSeat() {
        return seat;
    }

//    public void setUser(User user) {
//        this.user = user;
//    }

//    public void setEvent(Event event) {
//        this.event = event;
//    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

//    public long getUserId() {
//		return userId;
//	}
//
//	public void setUserId(long userId) {
//		this.userId = userId;
//	}

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

//    @Override
//    public int compareTo(Ticket other) {
//        if (other == null) {
//            return 1;
//        }
//        int result = dateTime.compareTo(other.getDateTime());
//
//        if (result == 0) {
//            result = event.getName().compareTo(other.getEvent().getName());
//        }
//        if (result == 0) {
//            result = Long.compare(seat, other.getSeat());
//        }
//        return result;
//    }

}
