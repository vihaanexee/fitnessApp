// src/main/java/org/example/services/UserBookingService.java
package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.entities.Ticket;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class UserBookingService {
    private User user;
    private List<User> userList;
    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String USERS_PATH = "src/main/resources/users.json";
    private static final String TRAINS_PATH = "src/main/resources/trains.json";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Constructor without user (for signup)
    public UserBookingService() throws IOException {
        loadData();
    }

    // Constructor with user (for login)
    public UserBookingService(User user) throws IOException {
        this.user = user;
        loadData();
        if (!loginUser()) {
            throw new IOException("Invalid credentials");
        }
    }

    private void loadData() throws IOException {
        // Load users
        File usersFile = new File(USERS_PATH);
        if (usersFile.exists()) {
            userList = objectMapper.readValue(usersFile, new TypeReference<List<User>>() {});
        } else {
            userList = new ArrayList<>();
        }

        // Load trains
        File trainsFile = new File(TRAINS_PATH);
        if (trainsFile.exists()) {
            trainList = objectMapper.readValue(trainsFile, new TypeReference<List<Train>>() {});
        } else {
            trainList = initializeDefaultTrains();
        }
    }

    private List<Train> initializeDefaultTrains() {
        List<Train> trains = new ArrayList<>();

        // Create sample trains
        Map<String, String> train1Times = new HashMap<>();
        train1Times.put("Delhi", "08:00");
        train1Times.put("Gurgaon", "09:00");
        train1Times.put("Mumbai", "18:00");

        List<List<Integer>> seats1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Integer> row = new ArrayList<>(Arrays.asList(0, 0, 0, 0)); // 0 = available, 1 = booked
            seats1.add(row);
        }

        trains.add(new Train("T001", "12345", seats1, train1Times, Arrays.asList("Delhi", "Gurgaon", "Mumbai")));

        return trains;
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> user1.getName().equals(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()))
                .findFirst();

        if (foundUser.isPresent()) {
            this.user = foundUser.get();
            return true;
        }
        return false;
    }

    public Boolean signUp(User user1) {
        try {
            // Check if user already exists
            boolean userExists = userList.stream()
                    .anyMatch(existingUser -> Objects.equals(existingUser.getName(), user1.getName()));
            if (userExists) {
                System.out.println("User already exists!");
                return false;
            }

            userList.add(user1);
            saveUserListToFile();
            System.out.println("User registered successfully!");
            return true;
        } catch (IOException ex) {
            System.out.println("Error during signup: " + ex.getMessage());
            return false;
        }
    }

    public void fetchBookings() {
        if (user == null) {
            System.out.println("Please login first!");
            return;
        }

        List<Ticket> tickets = user.getTicketsBooked();
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("No bookings found!");
            return;
        }

        System.out.println("Your bookings:");
        for (Ticket ticket : tickets) {
            System.out.println(ticket.getTicketInfo());
        }
    }

    public List<Train> getTrains(String source, String destination) {
        return trainList.stream()
                .filter(train -> train.getStations().contains(source) &&
                        train.getStations().contains(destination))
                .collect(Collectors.toList());
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int col, String source, String destination) {
        if (user == null) {
            System.out.println("Please login first!");
            return false;
        }

        List<List<Integer>> seats = train.getSeats();

        // Check if seat is available
        if (row < 0 || row >= seats.size() || col < 0 || col >= seats.get(row).size()) {
            System.out.println("Invalid seat selection!");
            return false;
        }

        if (seats.get(row).get(col) == 1) {
            System.out.println("Seat already booked!");
            return false;
        }

        // Book the seat
        seats.get(row).set(col, 1);

        // Create ticket with proper source and destination
        Ticket ticket = new Ticket(
                UUID.randomUUID().toString(),
                user.getUserId(),
                source,
                destination,
                dateFormat.format(new Date()),
                train
        );

        // Add ticket to user's bookings
        if (user.getTicketsBooked() == null) {
            user.setTicketsBooked(new ArrayList<>());
        }
        user.getTicketsBooked().add(ticket);

        // Update the user in the userList
        updateUserInList();

        try {
            saveUserListToFile();
            saveTrainListToFile();
            System.out.println("Ticket booked successfully! Ticket ID: " + ticket.getTicketId());
            return true;
        } catch (IOException e) {
            System.out.println("Error saving booking: " + e.getMessage());
            return false;
        }
    }

    // Overloaded method for backward compatibility
    public Boolean bookTrainSeat(Train train, int row, int col) {
        List<String> stations = train.getStations();
        String source = stations.get(0);
        String destination = stations.get(stations.size() - 1);

        return bookTrainSeat(train, row, col, source, destination);
    }

    private void updateUserInList() {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(user.getUserId())) {
                userList.set(i, user);
                break;
            }
        }
    }

    public List<Ticket> getCurrentUserTickets() {
        if (user == null) {
            return new ArrayList<>();
        }
        return user.getTicketsBooked() != null ? user.getTicketsBooked() : new ArrayList<>();
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        usersFile.getParentFile().mkdirs();
        objectMapper.writeValue(usersFile, userList);
    }

    private void saveTrainListToFile() throws IOException {
        File trainsFile = new File(TRAINS_PATH);
        trainsFile.getParentFile().mkdirs();
        objectMapper.writeValue(trainsFile, trainList);
    }

    public void saveUser() {
        try {
            saveUserListToFile();
        } catch (IOException e) {
            System.out.println("Error saving user data: " + e.getMessage());
        }
    }

    public User findUserByName(String nameToLogin){
        return userList.stream()
                .filter(user -> user.getName().equals(nameToLogin))
                .findFirst()
                .orElse(null);
    }
}