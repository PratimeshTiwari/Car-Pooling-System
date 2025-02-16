// Main.java
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
public class Main {
    private static CarpoolingSystem system = new CarpoolingSystem();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            try {
                System.out.println("\n=== Carpooling System ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // clear invalid input
                    continue;
                }

                switch (choice) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        loginUser();
                        break;
                    case 3:
                        System.out.println("Thank you for using our Carpooling System!");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option! Please choose 1-3.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                scanner.nextLine(); // clear any remaining input
            }
        }
    }

    private static void registerUser() {
        try {
            System.out.println("\n=== Registration ===");
            System.out.println("1. Register as Driver");
            System.out.println("2. Register as Rider");

            int type;
            while (true) {
                try {
                    System.out.print("Choose user type (1 or 2): ");
                    type = scanner.nextInt();
                    if (type != 1 && type != 2) {
                        System.out.println("Please enter either 1 or 2.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            scanner.nextLine(); // consume newline

            System.out.print("Enter userId: ");
            String userId = scanner.nextLine().trim();
            if (userId.isEmpty()) {
                throw new IllegalArgumentException("User ID cannot be empty");
            }

            System.out.print("Enter name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty");
            }

            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();
            if (!email.contains("@")) {
                throw new IllegalArgumentException("Invalid email format");
            }

            if (type == 1) {
                registerDriver(userId, name, password, email);
            } else {
                registerRider(userId, name, password, email);
            }

            System.out.println("Registration successful!");
        } catch (IllegalArgumentException e) {
            System.out.println("Registration failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during registration.");
            e.printStackTrace();
        }
    }

    private static void registerDriver(String userId, String name, String password, String email) {
        try {
            System.out.print("Enter car model: ");
            String carModel = scanner.nextLine().trim();
            if (carModel.isEmpty()) {
                throw new IllegalArgumentException("Car model cannot be empty");
            }

            System.out.print("Enter license number: ");
            String license = scanner.nextLine().trim();
            if (license.isEmpty()) {
                throw new IllegalArgumentException("License number cannot be empty");
            }

            int seats;
            while (true) {
                try {
                    System.out.print("Enter available seats (1-10): ");
                    seats = scanner.nextInt();
                    if (seats < 1 || seats > 10) {
                        System.out.println("Please enter a number between 1 and 10.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            scanner.nextLine(); // consume newline

            Driver driver = new Driver(userId, name, password, email, carModel, license, seats);
            if (!system.registerUser(driver)) {
                throw new IllegalArgumentException("User ID already exists");
            }
        } catch (IllegalArgumentException e) {
            throw e; // Rethrow to be caught by the outer try-catch
        }
    }

    private static void registerRider(String userId, String name, String password, String email) {
        try {
            System.out.print("Enter preferred pickup location: ");
            String pickup = scanner.nextLine().trim();
            if (pickup.isEmpty()) {
                throw new IllegalArgumentException("Pickup location cannot be empty");
            }

            Rider rider = new Rider(userId, name, password, email, pickup);
            if (!system.registerUser(rider)) {
                throw new IllegalArgumentException("User ID already exists");
            }
        } catch (IllegalArgumentException e) {
            throw e; // Rethrow to be caught by the outer try-catch
        }
    }

    private static void loginUser() {
        try {
            System.out.print("Enter userId: ");
            String userId = scanner.nextLine().trim();
            if (userId.isEmpty()) {
                System.out.println("User ID cannot be empty.");
                return;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty.");
                return;
            }

            User user = system.login(userId, password);
            if (user != null) {
                System.out.println("Login successful!");
                if (user instanceof Driver) {
                    showDriverMenu((Driver) user);
                } else {
                    showRiderMenu((Rider) user);
                }
            } else {
                System.out.println("Login failed! Invalid credentials.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during login. Please try again.");
        }
    }

    // Add methods for driver and rider menus
    private static void showDriverMenu(Driver driver) {
        while (true) {
            try {
                System.out.println("\n=== Driver Menu ===");
                System.out.println("1. Create New Carpool");
                System.out.println("2. View My Active Carpools");
                System.out.println("3. View My Riders");
                System.out.println("4. Manage Ride Requests");
                System.out.println("5. Logout");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // clear invalid input
                    continue;
                }

                switch (choice) {
                    case 1:
                        createNewCarpool(driver);
                        break;
                    case 2:
                        viewMyActiveCarpools(driver);
                        break;
                    case 3:
                        viewMyRiders(driver);
                        break;
                    case 4:
                        manageRideRequests(driver);
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("Invalid option! Please choose 1-5.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                scanner.nextLine(); // clear any remaining input
            }
        }
    }

    // Add this new method to handle ride requests
    private static void manageRideRequests(Driver driver) {
        System.out.println("\n=== Manage Ride Requests ===");
        List<CarPool> myCarpools = system.getDriverCarpools(driver.getUserId());

        if (myCarpools.isEmpty()) {
            System.out.println("You have no active carpools.");
            return;
        }

        boolean hasRequests = false;
        for (CarPool pool : myCarpools) {
            List<Rider> pendingRequests = pool.getPendingRequests();
            if (!pendingRequests.isEmpty()) {
                hasRequests = true;
                System.out.println("\nPool ID: " + pool.getPoolId());
                System.out.println("From: " + pool.getSource() + " To: " + pool.getDestination());
                System.out.println("Date/Time: " + pool.getDateTime());
                System.out.println("\nPending Requests:");

                int index = 1;
                for (Rider rider : pendingRequests) {
                    System.out.println(index++ + ". Rider: " + rider.getName());
                    System.out.println("   Email: " + rider.getEmail());
                    System.out.println("   Preferred pickup: " + rider.getPreferredPickupLocation());
                }

                System.out.println("\nActions for this pool:");
                System.out.println("1. Approve/Reject requests");
                System.out.println("2. Skip to next pool");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    for (Rider rider : new ArrayList<>(pendingRequests)) {
                        System.out.println("\nRider: " + rider.getName());
                        System.out.print("Approve this request? (y/n): ");
                        boolean approve = scanner.nextLine().toLowerCase().startsWith("y");

                        if (approve) {
                            if (pool.approveRequest(rider)) {
                                System.out.println("Request approved!");
                            } else {
                                System.out.println("Failed to approve request. No seats available.");
                            }
                        } else {
                            pool.rejectRequest(rider);
                            System.out.println("Request rejected.");
                        }
                    }
                }
            }
        }

        if (!hasRequests) {
            System.out.println("No pending ride requests to manage.");
        }
    }

    private static void showRiderMenu(Rider rider) {
        while (true) {
            try {
                System.out.println("\n=== Rider Menu ===");
                System.out.println("1. View Available Carpools");
                System.out.println("2. Search and Join a Carpool");
                System.out.println("3. View My Bookings");
                System.out.println("4. Logout");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // clear invalid input
                    continue;
                }

                switch (choice) {
                    case 1:
                        viewAvailableCarpools();
                        break;
                    case 2:
                        searchAndJoinPool(rider);
                        break;
                    case 3:
                        viewMyBookings(rider);
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid option! Please choose 1-4.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred. Please try again.");
                scanner.nextLine(); // clear any remaining input
            }
        }
    }


    private static void viewMyActiveCarpools(Driver driver) {
        System.out.println("\n=== My Active Carpools ===");
        List<CarPool> myCarpools = system.getDriverCarpools(driver.getUserId());

        if (myCarpools.isEmpty()) {
            System.out.println("You have no active carpools.");
            return;
        }

        for (CarPool pool : myCarpools) {
            System.out.println("\nPool ID: " + pool.getPoolId());
            System.out.println("From: " + pool.getSource());
            System.out.println("To: " + pool.getDestination());
            System.out.println("Date/Time: " + pool.getDateTime());
            System.out.println("Available Seats: " + pool.getAvailableSeats());
            System.out.println("Price per seat: $" + pool.getPrice());
        }
    }

    private static void viewMyRiders(Driver driver) {
        System.out.println("\n=== My Riders ===");
        List<CarPool> myCarpools = system.getDriverCarpools(driver.getUserId());

        if (myCarpools.isEmpty()) {
            System.out.println("You have no active carpools.");
            return;
        }

        for (CarPool pool : myCarpools) {
            System.out.println("\nPool ID: " + pool.getPoolId());
            List<Rider> riders = pool.getRiders();
            if (riders.isEmpty()) {
                System.out.println("No riders in this pool yet.");
            } else {
                for (Rider rider : riders) {
                    System.out.println("- " + rider.getName() + " (Pickup: " +
                            rider.getPreferredPickupLocation() + ")");
                }
            }
        }
    }

    // Helper methods for Rider menu
    private static void viewAvailableCarpools() {
        System.out.println("\n=== Available Carpools ===");
        List<CarPool> availablePools = system.getAvailableCarpools();

        if (availablePools.isEmpty()) {
            System.out.println("No available carpools at the moment.");
            return;
        }

        for (CarPool pool : availablePools) {
            System.out.println("\nPool ID: " + pool.getPoolId());
            System.out.println("Driver: " + pool.getDriver().getName());
            System.out.println("From: " + pool.getSource() +
                    " (Pickup: " + pool.getExactPickupLocation() + ")");
            System.out.println("To: " + pool.getDestination() +
                    " (Drop: " + pool.getExactDropLocation() + ")");
            System.out.println("Date/Time: " + pool.getDateTime());
            System.out.println("Available Seats: " + pool.getAvailableSeats());
            System.out.println("Price per seat: $" + pool.getPrice());
            System.out.println("Preferences:");
            System.out.println("- Smoking: " + (pool.isSmokingAllowed() ? "Allowed" : "Not allowed"));
            System.out.println("- Pets: " + (pool.isPetsAllowed() ? "Allowed" : "Not allowed"));
            System.out.println("- Music: " + (pool.isMusicAllowed() ? "Allowed" : "Not allowed"));
            if (pool.isFemaleOnly()) System.out.println("- Female riders only");
            if (!pool.getAdditionalRules().isEmpty())
                System.out.println("Additional Rules: " + pool.getAdditionalRules());
        }
    }

    private static void joinCarpool(Rider rider) {
        System.out.println("\n=== Join a Carpool ===");
        viewAvailableCarpools();

        System.out.print("\nEnter Pool ID to join (or 0 to cancel): ");
        String poolId = scanner.nextLine();

        if (poolId.equals("0")) {
            return;
        }

        boolean success = system.requestToJoinPool(poolId, rider);
        if (success) {
            System.out.println("Successfully joined the carpool!");
        } else {
            System.out.println("Failed to join the carpool. It might be full or invalid Pool ID.");
        }
    }

    private static void viewMyBookings(Rider rider) {
        System.out.println("\n=== My Bookings ===");
        List<CarPool> myBookings = system.getRiderBookings(rider.getUserId());

        if (myBookings.isEmpty()) {
            System.out.println("You have no active bookings.");
            return;
        }

        for (CarPool pool : myBookings) {
            System.out.println("\nPool ID: " + pool.getPoolId());
            System.out.println("Driver: " + pool.getDriver().getName());
            System.out.println("From: " + pool.getSource() +
                    " (Pickup: " + pool.getExactPickupLocation() + ")");
            System.out.println("To: " + pool.getDestination() +
                    " (Drop: " + pool.getExactDropLocation() + ")");
            System.out.println("Date/Time: " + pool.getDateTime());
            System.out.println("Price: $" + pool.getPrice());
            System.out.println("Status: " + pool.getStatus());
            System.out.println("Ride Preferences:");
            System.out.println("- Smoking: " + (pool.isSmokingAllowed() ? "Allowed" : "Not allowed"));
            System.out.println("- Pets: " + (pool.isPetsAllowed() ? "Allowed" : "Not allowed"));
            System.out.println("- Music: " + (pool.isMusicAllowed() ? "Allowed" : "Not allowed"));
        }
    }

    private static void createNewCarpool(Driver driver) {
        try {
            System.out.println("\n=== Create New Carpool ===");

            System.out.print("Enter source city: ");
            String source = scanner.nextLine().trim();
            if (source.isEmpty()) {
                throw new IllegalArgumentException("Source city cannot be empty");
            }

            System.out.print("Enter exact pickup location: ");
            String exactPickup = scanner.nextLine().trim();
            if (exactPickup.isEmpty()) {
                throw new IllegalArgumentException("Pickup location cannot be empty");
            }

            System.out.print("Enter destination city: ");
            String destination = scanner.nextLine().trim();
            if (destination.isEmpty()) {
                throw new IllegalArgumentException("Destination city cannot be empty");
            }

            System.out.print("Enter exact drop location: ");
            String exactDrop = scanner.nextLine().trim();
            if (exactDrop.isEmpty()) {
                throw new IllegalArgumentException("Drop location cannot be empty");
            }

            System.out.print("Enter date (DD/MM/YYYY): ");
            String date = scanner.nextLine().trim();
            if (!date.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
                throw new IllegalArgumentException("Invalid date format. Use DD/MM/YYYY");
            }

            System.out.print("Enter time (HH:MM): ");
            String time = scanner.nextLine().trim();
            if (!time.matches("\\d{1,2}:\\d{2}")) {
                throw new IllegalArgumentException("Invalid time format. Use HH:MM");
            }

            int seats;
            while (true) {
                try {
                    System.out.print("Enter available seats (1-10): ");
                    seats = scanner.nextInt();
                    if (seats < 1 || seats > 10) {
                        System.out.println("Please enter a number between 1 and 10.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }

            double price;
            while (true) {
                try {
                    System.out.print("Enter price per seat: ");
                    price = scanner.nextDouble();
                    if (price < 0) {
                        System.out.println("Price cannot be negative.");
                        continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid price.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            scanner.nextLine(); // consume newline

            System.out.print("Allow smoking? (y/n): ");
            boolean smoking = scanner.nextLine().toLowerCase().startsWith("y");

            System.out.print("Allow pets? (y/n): ");
            boolean pets = scanner.nextLine().toLowerCase().startsWith("y");

            System.out.print("Allow music? (y/n): ");
            boolean music = scanner.nextLine().toLowerCase().startsWith("y");

            System.out.print("Female riders only? (y/n): ");
            boolean femaleOnly = scanner.nextLine().toLowerCase().startsWith("y");

            System.out.print("Additional rules (if any): ");
            String rules = scanner.nextLine().trim();

            String poolId = system.createCarPool(driver, source, destination,
                    exactPickup, exactDrop,
                    date + " " + time, seats, price,
                    smoking, pets, music, femaleOnly, rules);

            System.out.println("Carpool created successfully! Pool ID: " + poolId);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create carpool: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while creating the carpool.");
            e.printStackTrace();
        }
    }

    private static int getMenuChoice(int min, int max) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
    private static int getIntegerInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    private static int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private static double getDoubleInput(String prompt, double min) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (value >= min) {
                    return value;
                }
                System.out.printf("Please enter a number greater than or equal to %.2f.\n", min);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
//    private static void searchAndJoinPool(Rider rider) {
//        System.out.println("\n=== Search for Carpools with Preferences ===");
//
//        System.out.print("Enter source city (or press Enter to skip): ");
//        String source = scanner.nextLine().trim();
//
//        System.out.print("Enter destination city (or press Enter to skip): ");
//        String destination = scanner.nextLine().trim();
//
//        System.out.print("Enter date (DD/MM/YYYY) (or press Enter to skip): ");
//        String date = scanner.nextLine().trim();
//
//        // Preferences
//        Map<String, Boolean> preferences = new HashMap<>();
//
//        // Ask for all preferences
//        System.out.println("\nRide Preferences:");
//
//        System.out.print("Filter for non-smoking rides? (y/n): ");
//        if (scanner.nextLine().toLowerCase().startsWith("y")) {
//            preferences.put("smokingAllowed", false);
//        }
//
//        System.out.print("Filter for no pets allowed? (y/n): ");
//        if (scanner.nextLine().toLowerCase().startsWith("y")) {
//            preferences.put("petsAllowed", false);
//        }
//
//        System.out.print("Filter for music allowed? (y/n): ");
//        if (scanner.nextLine().toLowerCase().startsWith("y")) {
//            preferences.put("musicAllowed", true);
//        }
//
//        System.out.print("Filter for female-only rides? (y/n): ");
//        if (scanner.nextLine().toLowerCase().startsWith("y")) {
//            preferences.put("femaleOnly", true);
//        }
//
//        // Add price range filter
//        System.out.print("Maximum price willing to pay (or 0 for no limit): ");
//        double maxPrice = scanner.nextDouble();
//        scanner.nextLine(); // consume newline
//
//        List<CarPool> availablePools = system.searchCarpools(source, destination, date, preferences)
//                .stream()
//                .filter(cp -> maxPrice == 0 || cp.getPrice() <= maxPrice)
//                .collect(Collectors.toList());
//
//        if (availablePools.isEmpty()) {
//            System.out.println("\nNo matching carpools found.");
//            System.out.println("Tips:");
//            System.out.println("- Try searching without preferences first");
//            System.out.println("- Use partial city names");
//            System.out.println("- Leave date empty to see all available rides");
//            System.out.println("- Reduce the number of preferences");
//            return;
//        }
//
//        System.out.println("\nFound " + availablePools.size() + " matching carpools:");
//
//        for (CarPool pool : availablePools) {
//            System.out.println("\n----------------------------------------");
//            System.out.println("Pool ID: " + pool.getPoolId());
//            System.out.println("Driver: " + pool.getDriver().getName());
//            System.out.println("From: " + pool.getSource());
//            System.out.println("Pickup Location: " + pool.getExactPickupLocation());
//            System.out.println("To: " + pool.getDestination());
//            System.out.println("Drop Location: " + pool.getExactDropLocation());
//            System.out.println("Date/Time: " + pool.getDateTime());
//            System.out.println("Available Seats: " + pool.getAvailableSeats());
//            System.out.println("Price per seat: $" + pool.getPrice());
//            System.out.println("Ride Preferences:");
//            System.out.println("- Smoking: " + (pool.isSmokingAllowed() ? "Allowed" : "Not allowed"));
//            System.out.println("- Pets: " + (pool.isPetsAllowed() ? "Allowed" : "Not allowed"));
//            System.out.println("- Music: " + (pool.isMusicAllowed() ? "Allowed" : "Not allowed"));
//            if (pool.isFemaleOnly()) System.out.println("- Female riders only");
//            if (!pool.getAdditionalRules().isEmpty())
//                System.out.println("Additional rules: " + pool.getAdditionalRules());
//            System.out.println("----------------------------------------");
//        }
//
//        System.out.print("\nEnter Pool ID to request joining (or 0 to cancel): ");
//        String poolId = scanner.nextLine();
//
//        if (!poolId.equals("0")) {
//            if (system.requestToJoinPool(poolId, rider)) {
//                System.out.println("Request sent successfully! Waiting for driver's approval.");
//            } else {
//                System.out.println("Failed to send request. Pool might be full or invalid.");
//            }
//        }
//    }
private static void searchAndJoinPool(Rider rider) {
    System.out.println("\n=== Search for Carpools with Preferences ===");

    System.out.print("Enter source city (or press Enter to skip): ");
    String source = scanner.nextLine().trim();

    System.out.print("Enter destination city (or press Enter to skip): ");
    String destination = scanner.nextLine().trim();

    System.out.print("Enter date (DD/MM/YYYY) (or press Enter to skip): ");
    String date = scanner.nextLine().trim();

    // Preferences
    Map<String, Boolean> preferences = new HashMap<>();

    // Ask for all preferences
    System.out.println("\nRide Preferences:");

    System.out.print("Filter for non-smoking rides? (y/n): ");
    if (scanner.nextLine().toLowerCase().startsWith("y")) {
        preferences.put("smokingAllowed", false);
    }

    System.out.print("Filter for no pets allowed? (y/n): ");
    if (scanner.nextLine().toLowerCase().startsWith("y")) {
        preferences.put("petsAllowed", false);
    }

    System.out.print("Filter for music allowed? (y/n): ");
    if (scanner.nextLine().toLowerCase().startsWith("y")) {
        preferences.put("musicAllowed", true);
    }

    System.out.print("Filter for female-only rides? (y/n): ");
    if (scanner.nextLine().toLowerCase().startsWith("y")) {
        preferences.put("femaleOnly", true);
    }

    // Add price range filter
    System.out.print("Maximum price willing to pay (or 0 for no limit): ");
    double maxPrice = scanner.nextDouble();
    scanner.nextLine(); // consume newline

    // Get exact matches
    List<CarPool> exactMatches = system.searchCarpools(source, destination, date, preferences)
            .stream()
            .filter(cp -> maxPrice == 0 || cp.getPrice() <= maxPrice)
            .collect(Collectors.toList());

    // Display exact matches if found
    if (!exactMatches.isEmpty()) {
        System.out.println("\nFound " + exactMatches.size() + " exact matches:");
        displayCarpools(exactMatches, true);
    } else {
        System.out.println("\nNo exact matches found with your preferences.");

        // Get all available rides without preferences
        List<CarPool> allAvailable = system.getAvailableCarpools();

        // First show rides with matching source/destination but different preferences
        List<CarPool> locationMatches = allAvailable.stream()
                .filter(cp -> (source.isEmpty() ||
                        cp.getSource().toLowerCase().contains(source.toLowerCase())) &&
                        (destination.isEmpty() ||
                                cp.getDestination().toLowerCase().contains(destination.toLowerCase())))
                .collect(Collectors.toList());

        if (!locationMatches.isEmpty()) {
            System.out.println("\nFound " + locationMatches.size() +
                    " rides with matching locations but different preferences:");
            displayCarpools(locationMatches, false);
        }

        // Then show other available rides
        List<CarPool> otherRides = allAvailable.stream()
                .filter(cp -> !locationMatches.contains(cp))
                .collect(Collectors.toList());

        if (!otherRides.isEmpty()) {
            System.out.println("\nOther available rides you might be interested in:");
            displayCarpools(otherRides, false);
        }

        System.out.println("\nTips for better matches:");
        System.out.println("- Try searching without preferences");
        System.out.println("- Use partial city names");
        System.out.println("- Leave date empty to see all available rides");
        System.out.println("- Adjust your price range");
    }

    System.out.print("\nEnter Pool ID to request joining (or 0 to cancel): ");
    String poolId = scanner.nextLine();

    if (!poolId.equals("0")) {
        if (system.requestToJoinPool(poolId, rider)) {
            System.out.println("Request sent successfully! Waiting for driver's approval.");
        } else {
            System.out.println("Failed to send request. Pool might be full or invalid.");
        }
    }
}

    // Helper method to display carpools
    private static void displayCarpools(List<CarPool> carpools, boolean isExactMatch) {
        for (CarPool pool : carpools) {
            System.out.println("\n----------------------------------------");
            if (isExactMatch) {
                System.out.println("EXACT MATCH");
            }
            System.out.println("Pool ID: " + pool.getPoolId());
            System.out.println("Driver: " + pool.getDriver().getName());
            System.out.println("From: " + pool.getSource());
            System.out.println("Pickup Location: " + pool.getExactPickupLocation());
            System.out.println("To: " + pool.getDestination());
            System.out.println("Drop Location: " + pool.getExactDropLocation());
            System.out.println("Date/Time: " + pool.getDateTime());
            System.out.println("Available Seats: " + pool.getAvailableSeats());
            System.out.println("Price per seat: $" + pool.getPrice());
            System.out.println("Ride Preferences:");
            System.out.println("- Smoking: " + (pool.isSmokingAllowed() ? "Allowed" : "Not allowed"));
            System.out.println("- Pets: " + (pool.isPetsAllowed() ? "Allowed" : "Not allowed"));
            System.out.println("- Music: " + (pool.isMusicAllowed() ? "Allowed" : "Not allowed"));
            if (pool.isFemaleOnly()) System.out.println("- Female riders only");
            if (!pool.getAdditionalRules().isEmpty())
                System.out.println("Additional rules: " + pool.getAdditionalRules());
            System.out.println("----------------------------------------");
        }
    }
}
