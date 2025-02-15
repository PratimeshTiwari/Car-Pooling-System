package com.test.carpool;

import java.util.*;

final class CarPoolConstants {
    public static final double MATCH_THRESHOLD = 0.5;
    private CarPoolConstants() {}
}

// Main CarpoolSystem class
public class CarpoolSystem {
    // Mock storage
    private Map<String, User> users;
    private List<Ride> availableRides;
    private Map<String, List<Ride>> rideSearchCache;

    public CarpoolSystem() {
        this.users = new HashMap<>();
        this.availableRides = new ArrayList<>();
        this.rideSearchCache = new HashMap<>();
    }

    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Authentication successful for user: " + username);
            return true;
        }
        System.out.println("Authentication failed for user: " + username);
        return false;
    }

    public void registerUser(User user) {
        if (users.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("User already exists: " + user.getUsername());
        }
        users.put(user.getUsername(), user);
        System.out.println("User registered successfully: " + user.getUsername());
    }

    public void createRide(Driver driver, String startLocation, String endLocation,
                           Date departureTime, int availableSeats, String vehicleDetails) {
        Ride newRide = new Ride(driver, startLocation, endLocation, departureTime, availableSeats, vehicleDetails);
        availableRides.add(newRide);
        System.out.println("New ride created by " + driver.getUsername()
                + " from " + startLocation + " to " + endLocation);
    }

    public List<Ride> searchRides(String origin, String destination) {
        String cacheKey = origin + "-" + destination;

        if (rideSearchCache.containsKey(cacheKey)) {
            System.out.println("Returning results from cache...");
            return rideSearchCache.get(cacheKey);
        }

        List<Ride> matchedRides = new ArrayList<>();
        for (Ride ride : availableRides) {
            if (ride.getStartLocation().equalsIgnoreCase(origin)
                    && ride.getEndLocation().equalsIgnoreCase(destination)
                    && ride.getAvailableSeats() > 0) {
                matchedRides.add(ride);
            }
        }

        rideSearchCache.put(cacheKey, matchedRides);
        return matchedRides;
    }

    public double calculateRouteMatchPercentage(String driverStart, String driverEnd,
                                                String riderStart, String riderEnd) {
        double matchScore = 0.0;

        if (driverStart.equalsIgnoreCase(riderStart)) {
            matchScore += 0.5;
        }
        if (driverEnd.equalsIgnoreCase(riderEnd)) {
            matchScore += 0.5;
        }

        return matchScore * 100;
    }

    public void requestToJoin(Ride ride, Rider rider) {
        double matchPercent = calculateRouteMatchPercentage(
                ride.getStartLocation(),
                ride.getEndLocation(),
                rider.getStartLocationPreference(),
                rider.getEndLocationPreference()
        );

        // Using the constant from CarPoolConstants
        if (matchPercent < CarPoolConstants.MATCH_THRESHOLD * 100) {
            System.out.println("Sorry, route mismatch. (" + matchPercent + "% match)");
            return;
        }

        if (ride.getAvailableSeats() > 0) {
            ride.getPassengers().add(rider);
            ride.setAvailableSeats(ride.getAvailableSeats() - 1);
            System.out.println(rider.getUsername() + " joined the ride. Route match: " + matchPercent + "%");
        } else {
            System.out.println("No seats available for this ride.");
        }
    }

    public static void main(String[] args) {
        CarpoolSystem system = new CarpoolSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Carpool System Menu ===");
            System.out.println("1. Register as Driver");
            System.out.println("2. Register as Rider");
            System.out.println("3. Login");
            System.out.println("4. Create Ride (for Drivers)");
            System.out.println("5. Search and Join Ride (for Riders)");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // Register as Driver
                    System.out.print("Enter username: ");
                    String dUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String dPassword = scanner.nextLine();
                    System.out.print("Enter license number: ");
                    String license = scanner.nextLine();
                    System.out.print("Enter vehicle model: ");
                    String vehicleModel = scanner.nextLine();

                    Driver driver = new Driver(dUsername, dPassword, license, vehicleModel);
                    try {
                        system.registerUser(driver);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Registration failed: " + e.getMessage());
                    }
                    break;

                case 2: // Register as Rider
                    System.out.print("Enter username: ");
                    String rUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String rPassword = scanner.nextLine();
                    System.out.print("Enter preferred start location: ");
                    String startLoc = scanner.nextLine();
                    System.out.print("Enter preferred end location: ");
                    String endLoc = scanner.nextLine();

                    Rider rider = new Rider(rUsername, rPassword, startLoc, endLoc);
                    try {
                        system.registerUser(rider);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Registration failed: " + e.getMessage());
                    }
                    break;

                case 3: // Login
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    boolean isAuthenticated = system.authenticate(username, password);
                    if (isAuthenticated) {
                        System.out.println("Login successful!");
                    }
                    break;

                case 4: // Create Ride
                    System.out.print("Enter your username (driver): ");
                    String driverUsername = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String driverPassword = scanner.nextLine();

                    if (system.authenticate(driverUsername, driverPassword)) {
                        try {
                            System.out.print("Enter start location: ");
                            String startLocation = scanner.nextLine();
                            System.out.print("Enter end location: ");
                            String endLocation = scanner.nextLine();
                            System.out.print("Enter number of available seats: ");
                            int seats = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            System.out.print("Enter vehicle details: ");
                            String vehicleDetails = scanner.nextLine();

                            // Get current date/time for simplicity
                            Date departureTime = new Date();

                            Driver existingDriver = (Driver) system.users.get(driverUsername);
                            system.createRide(
                                    existingDriver,
                                    startLocation,
                                    endLocation,
                                    departureTime,
                                    seats,
                                    vehicleDetails
                            );
                        } catch (Exception e) {
                            System.out.println("Error creating ride: " + e.getMessage());
                        }
                    }
                    break;

                case 5: // Search and Join Ride
                    System.out.print("Enter your username (rider): ");
                    String riderUsername = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String riderPassword = scanner.nextLine();

                    if (system.authenticate(riderUsername, riderPassword)) {
                        System.out.print("Enter start location to search: ");
                        String searchStart = scanner.nextLine();
                        System.out.print("Enter destination to search: ");
                        String searchEnd = scanner.nextLine();

                        List<Ride> foundRides = system.searchRides(searchStart, searchEnd);
                        System.out.println("Found " + foundRides.size() + " ride(s).");

                        if (!foundRides.isEmpty()) {
                            System.out.println("\nAvailable Rides:");
                            for (int i = 0; i < foundRides.size(); i++) {
                                Ride ride = foundRides.get(i);
                                System.out.println((i + 1) + ". From: " + ride.getStartLocation() +
                                        " To: " + ride.getEndLocation() +
                                        " (Seats available: " + ride.getAvailableSeats() + ")");
                            }

                            System.out.print("Enter ride number to join (1-" + foundRides.size() + "): ");
                            int rideChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            if (rideChoice > 0 && rideChoice <= foundRides.size()) {
                                Rider existingRider = (Rider) system.users.get(riderUsername);
                                system.requestToJoin(foundRides.get(rideChoice - 1), existingRider);
                            } else {
                                System.out.println("Invalid ride number.");
                            }
                        }
                    }
                    break;

                case 6: // Exit
                    System.out.println("Thank you for using the Carpool System!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

abstract class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}

class Driver extends User {
    private String licenseNumber;
    private String vehicleModel;

    public Driver(String username, String password, String licenseNumber, String vehicleModel) {
        super(username, password);
        this.licenseNumber = licenseNumber;
        this.vehicleModel = vehicleModel;
    }
}

class Rider extends User {
    private String startLocationPreference;
    private String endLocationPreference;

    public Rider(String username, String password, String startLoc, String endLoc) {
        super(username, password);
        this.startLocationPreference = startLoc;
        this.endLocationPreference = endLoc;
    }

    public String getStartLocationPreference() { return startLocationPreference; }
    public String getEndLocationPreference() { return endLocationPreference; }
}

class Ride {
    private Driver driver;
    private String startLocation;
    private String endLocation;
    private Date departureTime;
    private int availableSeats;
    private String vehicleDetails;
    private List<Rider> passengers;

    public Ride(Driver driver, String startLocation, String endLocation,
                Date departureTime, int availableSeats, String vehicleDetails) {
        this.driver = driver;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.departureTime = departureTime;
        this.availableSeats = availableSeats;
        this.vehicleDetails = vehicleDetails;
        this.passengers = new ArrayList<>();
    }

    public String getStartLocation() { return startLocation; }
    public String getEndLocation() { return endLocation; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int seats) { this.availableSeats = seats; }
    public List<Rider> getPassengers() { return passengers; }
}