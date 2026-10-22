package by.ipprpo.rental.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Rental {
    private final int id;
    private final Customer customer;
    private final Bicycle bicycle;
    private final LocalDateTime startTime;
    private LocalDateTime endTime; // null, если аренда активна

    public Rental(int id, Customer customer, Bicycle bicycle, LocalDateTime startTime) {
        this.id = id;
        this.customer = customer;
        this.bicycle = bicycle;
        this.startTime = startTime;
        this.endTime = null;
    }

    public int getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Bicycle getBicycle() { return bicycle; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isActive() {
        return endTime == null;
    }

    @Override
    public String toString() {
        return "Rental{id=" + id + ", customer=" + customer.getName() +
                ", bicycle=" + bicycle.getModel() + ", start=" + startTime +
                ", end=" + endTime + '}';
    }
}