package ua.epam.spring.hometask.domain;

import java.util.Objects;

public class OrderEntry extends DomainObject {

    private double basePrice;
    private double discount;
    private long orderId;
    
    public OrderEntry() {
    }

    public OrderEntry(OrderEntry orderEntry) {
        super(orderEntry);
        this.basePrice = orderEntry.getBasePrice();
        this.discount = orderEntry.discount;
        this.orderId = orderEntry.orderId;
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

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	@Override
    public int hashCode() {
        return Objects.hash(orderId, basePrice, discount);
    }
    
    @Override
    public boolean equals(Object obj) {
        OrderEntry other = (OrderEntry) obj;
        return super.equals(other) && 
        		// do we need this
        		Objects.equals(getId(), other.getId()) &&
        		Objects.equals(orderId, other.orderId) &&
                Objects.equals(basePrice, other.basePrice) && 
                Objects.equals(discount, other.discount);
    }

}
