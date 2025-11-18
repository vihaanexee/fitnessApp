// src/main/java/org/example/entities/Ticket.java
package org.example.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ticket {
    @JsonProperty("ticketId")
    private String ticketId;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("source")
    private String source;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("dateTime")
    private String dateTime;

    @JsonProperty("train")
    private Train train;

    @JsonProperty("seatRow")
    private int seatRow;

    @JsonProperty("seatCol")
    private int seatCol;

    // Default constructor
    public Ticket() {}

    // Constructor
    public Ticket(String ticketId, String userId, String source, String destination, String dateTime, Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateTime = dateTime;
        this.train = train;
    }

    // Constructor with seat info
    public Ticket(String ticketId, String userId, String source, String destination, String dateTime, Train train, int seatRow, int seatCol) {
        this(ticketId, userId, source, destination, dateTime, train);
        this.seatRow = seatRow;
        this.seatCol = seatCol;
    }

    // Getters
    public String getTicketId() {
        return ticketId;
    }

    public String getUserId() {
        return userId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Train getTrain() {
        return train;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public int getSeatCol() {
        return seatCol;
    }

    // Setters
    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    // Utility method to get ticket information
    public String getTicketInfo() {
        return String.format("Ticket ID: %s | From: %s | To: %s | Date: %s | Train: %s | Seat: %d-%d",
                ticketId, source, destination, dateTime,
                train != null ? train.getTrainId() : "N/A", seatRow, seatCol);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", userId='" + userId + '\'' +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", seatRow=" + seatRow +
                ", seatCol=" + seatCol +
                '}';
    }
}