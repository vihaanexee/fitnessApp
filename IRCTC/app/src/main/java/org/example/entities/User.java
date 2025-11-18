package org.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class User {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("password")
    private String password;

    @JsonProperty("hashedPassword")
    private String hashedPassword;

    @JsonProperty("ticketsBooked")
    private List<Ticket> ticketsBooked;

    public User() {
        this.ticketsBooked = new ArrayList<>();
    }

    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.ticketsBooked = new ArrayList<>();
    }


    public <E> User(String nameToSignUp, String passwordToSignUp, String hashedSignupPwd, ArrayList<E> es, String string) {
    }

    public User(String userId, String nameToSignUp, String passwordToSignUp, String hashedSignupPwd, ArrayList<Object> objects) {
    }

    // Remove this constructor unless really needed and used correctly
    // public <E> User(String nameToSignUp, String passwordToSignUp, String hashedSignupPwd, ArrayList<E> es, String string) {}

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", ticketsBooked=" + (ticketsBooked != null ? ticketsBooked.size() : 0) + " tickets" +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId != null ? userId.equals(user.userId) : user.userId == null;
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    public void setBookings(ArrayList<Object> objects) {
        if (this.ticketsBooked == null) {
            this.ticketsBooked = new ArrayList<>();
        }
        for (Object obj : objects) {
            if (obj instanceof Ticket) {
                this.ticketsBooked.add((Ticket) obj);
            } else {
                System.out.println("Invalid object type: " + obj.getClass().getName());
            }
        }
    }

    public boolean getBookings() {
        if (this.ticketsBooked == null || this.ticketsBooked.isEmpty()) {
            System.out.println("No bookings found for user: " + name);
            return false;
        }
        System.out.println("Bookings for user: " + name);
        for (Ticket ticket : ticketsBooked) {
            System.out.println(ticket.getTicketInfo());
        }
        return true;
    }
}
