Introduction The Carpool Management System is a platform designed to facilitate ride-sharing by connecting drivers and riders. Users can register as drivers to offer rides or as riders to search for and book available seats.

Key Features • User Registration – Sign up as a driver or rider • Ride Search – Find available carpool rides based on location and timing • Seat Booking – Reserve seats in listed rides • Ride Management – Drivers can create, update, or cancel rides • Optimized Matching – Efficient ride allocation for a smoother experience

Scope The system enables user authentication, ride creation, ride searching, and ride booking. The platform aims to reduce travel costs, optimize fuel consumption, and enhance convenience.

General Description The system functions as a standalone carpooling platform with a user-friendly interface and database-driven backend.

Product Functions • User authentication • Ride creation (Driver) • Ride searching and booking (Rider) • Payment integration (Future Scope)

User Characteristics • Drivers: Provide rides, set routes, and accept passengers. • Riders: Search and book available rides.

Constraints • Only registered users can book rides

Assumptions and Dependencies • Users have mobile devices or computers with internet access. • External API services for geolocation and payments may be integrated later.

Functional Requirements

User registration and login authentication
Driver ride creation and availability status
Rider ride searching and booking
Real-time ride match algorithm
Payment integration (Future implementation)
Performance Requirements • The system should handle up to 10,000 users concurrently. • Response time for search queries should be < 3 seconds. • The system should provide 99.9% uptime.

Non-Functional Attributes • Security: Encrypted user authentication • Usability: Intuitive UI for both web and mobile • Scalability: Support increasing users over time


User Class
Purpose: Base class for all users in the system.

Variables:
- String userId: Unique identifier for the user.
- String name: Name of the user.
- String password: Password for authentication.
- String email: Email address for communication.

Methods:
- getUserId(): Returns the user ID.
- getName(): Returns the user's name.
- getPassword(): Returns the user's password.
- getEmail(): Returns the user's email.

---

Driver Class
Purpose: Represents a driver in the system.
Inherits: User

Variables:
- String carModel: Model of the driver's car.
- String licenseNumber: Driver's license number.
- int seatsAvailable: Number of available seats in the car.

Methods:
- getCarModel(): Returns the car model.
- getLicenseNumber(): Returns the license number.
- getSeatsAvailable(): Returns the number of available seats.

---

Rider Class
Purpose: Represents a rider in the system.
Inherits: User

Variables:
- String preferredPickupLocation: Preferred pickup location for the rider.

Methods:
- getPreferredPickupLocation(): Returns the preferred pickup location.

---

CarPool Class
File: CarPool.java
Purpose: Represents a single carpool ride.

Variables:
- String poolId: Unique identifier for the carpool.
- Driver driver: Driver of the carpool.
- String source: Source location.
- String destination: Destination location.
- String exactPickupLocation: Exact pickup location.
- String exactDropLocation: Exact drop location.
- String dateTime: Date and time of the carpool.
- int availableSeats: Number of available seats.
- double price: Price per seat.
- List<Rider> riders: List of confirmed riders.
- List<Rider> pendingRequests: List of pending rider requests.
- boolean smokingAllowed: Smoking preference.
- boolean petsAllowed: Pets preference.
- boolean musicAllowed: Music preference.
- boolean femaleOnly: Female-only preference.
- String additionalRules: Additional rules for the carpool.
- String status: Status of the carpool (ACTIVE, COMPLETED, CANCELLED).

Methods:
- getDriver(): Returns the driver.
- getAvailableSeats(): Returns the number of available seats.
- requestToJoin(Rider rider): Adds a rider to pending requests.
- approveRequest(Rider rider): Approves a pending request.
- rejectRequest(Rider rider): Rejects a pending request.
- isFull(): Checks if the carpool is full.
- isActive(): Checks if the carpool is active.
- completeRide(): Marks the carpool as completed.
- cancelRide(): Cancels the carpool.

---

CarpoolingSystem Class
File: CarpoolingSystem.java
Purpose: Manages the entire carpooling system.

Variables:
- List<CarPool> carpools: List of all carpools.
- Map<String, User> users: Map of user IDs to User objects.

Methods:
- registerUser(User user): Registers a new user.
- login(String userId, String password): Authenticates a user.
- createCarPool(Driver driver, ...): Creates a new carpool.
- getAvailableCarpools(): Returns a list of available carpools.
- searchCarpools(String source, String destination, ...): Searches for carpools based on criteria.
- requestToJoinPool(String poolId, Rider rider): Handles rider requests to join a carpool.
- approveRideRequest(String poolId, String riderId): Approves a rider's request.
- getCarPoolById(String poolId): Retrieves a carpool by its ID.
- getDriverCarpools(String driverId): Retrieves all carpools for a specific driver.
- getRiderBookings(String riderId): Retrieves all bookings for a specific rider.

---

Main Class
Purpose: Provides the user interface and handles user interactions.

Methods:
- main(String[] args): Entry point of the application.
- registerUser(): Handles user registration.
  - Calls registerDriver() or registerRider().
- registerDriver(String userId, ...): Registers a new driver.
  - Calls system.registerUser(driver).
- registerRider(String userId, ...): Registers a new rider.
  - Calls system.registerUser(rider).
- loginUser(): Handles user login.
  - Calls system.login(userId, password).
- showDriverMenu(Driver driver): Displays the driver menu.
  - Calls createNewCarpool(), viewMyActiveCarpools(), viewMyRiders(), manageRideRequests().
- showRiderMenu(Rider rider): Displays the rider menu.
  - Calls viewAvailableCarpools(), searchAndJoinPool(), viewMyBookings().
- createNewCarpool(Driver driver): Allows a driver to create a new carpool.
  - Calls system.createCarPool(driver, ...).
- viewMyActiveCarpools(Driver driver): Displays active carpools for a driver.
  - Calls system.getDriverCarpools(driver.getUserId()).
- viewMyRiders(Driver driver): Displays riders for a driver's carpools.
  - Calls system.getDriverCarpools(driver.getUserId()).
- manageRideRequests(Driver driver): Manages ride requests for a driver.
  - Calls system.getDriverCarpools(driver.getUserId()).
- viewAvailableCarpools(): Displays available carpools.
  - Calls system.getAvailableCarpools().
- searchAndJoinPool(Rider rider): Allows a rider to search and join a carpool.
  - Calls system.searchCarpools(...).
- viewMyBookings(Rider rider): Displays bookings for a rider.
  - Calls system.getRiderBookings(rider.getUserId()).



Note : This is a work in progress . Features are begin developed and added.
