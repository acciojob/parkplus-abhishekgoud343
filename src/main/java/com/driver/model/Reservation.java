package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int numberOfHours;

    private int bill;

    @JoinColumn
    @ManyToOne
    private User user;

    @JoinColumn
    @ManyToOne
    private Spot spot;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    public Reservation(Integer id, int numberOfHours, int bill, User user, Spot spot, Payment payment) {
        this.id = id;
        this.numberOfHours = numberOfHours;
        this.bill = bill;
        this.user = user;
        this.spot = spot;
        this.payment = payment;
    }

    public Reservation() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}