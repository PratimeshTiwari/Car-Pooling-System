// Add these imports at the top of CarpoolingSystem.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarpoolingSystem {
    private List<Driver> drivers;
    private List<Rider> riders;
    private List<CarPool> carpools;
    private Map<String, User> users;

    public CarpoolingSystem() {
        drivers = new ArrayList<>();
        riders = new ArrayList<>();
        carpools = new ArrayList<>();
        users = new HashMap<>();
    }

    // Create carpool method
    public String createCarPool(Driver driver,
                                String source,
                                String destination,
                                String exactPickupLocation,
                                String exactDropLocation,
                                String dateTime,
                                int seats,
                                double price,
                                boolean smokingAllowed,
                                boolean petsAllowed,
                                boolean musicAllowed,
                                boolean femaleOnly,
                                String additionalRules) {
        String poolId = "POOL" + (carpools.size() + 1);
        CarPool carPool = new CarPool(poolId,
                driver,
                source,
                destination,
                exactPickupLocation,
                exactDropLocation,
                dateTime,
                seats,
                price,
                smokingAllowed,
                petsAllowed,
                musicAllowed,
                femaleOnly,
                additionalRules);
        carpools.add(carPool);
        return poolId;
    }

    // Authentication methods
    public boolean registerUser(User user) {
        if (!users.containsKey(user.getUserId())) {
            users.put(user.getUserId(), user);
            if (user instanceof Driver) {
                drivers.add((Driver) user);
            } else if (user instanceof Rider) {
                riders.add((Rider) user);
            }
            return true;
        }
        return false;
    }

    public User login(String userId, String password) {
        User user = users.get(userId);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Carpool search and filter methods
    public List<CarPool> getAvailableCarpools() {
        return carpools.stream()
                .filter(cp -> cp.getAvailableSeats() > 0 && cp.isActive())
                .collect(Collectors.toList());
    }

    public List<CarPool> searchCarpools(String source, String destination,
                                        String date, Map<String, Boolean> preferences) {
        return carpools.stream()
                .filter(cp -> cp.isActive())
                .filter(cp -> source.isEmpty() ||
                        cp.getSource().toLowerCase().contains(source.toLowerCase()))
                .filter(cp -> destination.isEmpty() ||
                        cp.getDestination().toLowerCase().contains(destination.toLowerCase()))
                .filter(cp -> date.isEmpty() ||
                        cp.getDateTime().split(" ")[0].contains(date))
                .filter(cp -> {
                    if (preferences == null || preferences.isEmpty()) return true;

                    boolean matches = true;
                    if (preferences.containsKey("smokingAllowed"))
                        matches &= cp.isSmokingAllowed() == preferences.get("smokingAllowed");
                    if (preferences.containsKey("petsAllowed"))
                        matches &= cp.isPetsAllowed() == preferences.get("petsAllowed");
                    if (preferences.containsKey("musicAllowed"))
                        matches &= cp.isMusicAllowed() == preferences.get("musicAllowed");
                    if (preferences.containsKey("femaleOnly"))
                        matches &= cp.isFemaleOnly() == preferences.get("femaleOnly");

                    return matches;
                })
                .collect(Collectors.toList());
    }

    private boolean matchesPreferences(CarPool pool, Map<String, Boolean> preferences) {
        if (preferences == null) return true;

        boolean matches = true;
        if (preferences.containsKey("femaleOnly"))
            matches &= pool.isFemaleOnly() == preferences.get("femaleOnly");
        if (preferences.containsKey("smokingAllowed"))
            matches &= pool.isSmokingAllowed() == preferences.get("smokingAllowed");
        if (preferences.containsKey("petsAllowed"))
            matches &= pool.isPetsAllowed() == preferences.get("petsAllowed");
        if (preferences.containsKey("musicAllowed"))
            matches &= pool.isMusicAllowed() == preferences.get("musicAllowed");

        return matches;
    }

    // Ride request methods
    public boolean requestToJoinPool(String poolId, Rider rider) {
        CarPool pool = getCarPoolById(poolId);
        if (pool != null) {
            return pool.requestToJoin(rider);
        }
        return false;
    }

    public boolean approveRideRequest(String poolId, String riderId) {
        CarPool pool = getCarPoolById(poolId);
        if (pool != null) {
            Rider rider = (Rider) users.get(riderId);
            if (rider != null) {
                return pool.approveRequest(rider);
            }
        }
        return false;
    }

    // Utility methods
    public CarPool getCarPoolById(String poolId) {
        return carpools.stream()
                .filter(cp -> cp.getPoolId().equals(poolId))
                .findFirst()
                .orElse(null);
    }

    public List<CarPool> getDriverCarpools(String driverId) {
        return carpools.stream()
                .filter(cp -> cp.getDriver().getUserId().equals(driverId))
                .collect(Collectors.toList());
    }

    public List<CarPool> getRiderBookings(String riderId) {
        return carpools.stream()
                .filter(cp -> cp.getRiders().stream()
                        .anyMatch(r -> r.getUserId().equals(riderId)))
                .collect(Collectors.toList());
    }

    // Additional utility methods
    public int getTotalActiveCarpools() {
        return (int) carpools.stream()
                .filter(CarPool::isActive)
                .count();
    }

    public double getAveragePoolPrice() {
        return carpools.stream()
                .mapToDouble(CarPool::getPrice)
                .average()
                .orElse(0.0);
    }

    public List<Driver> getActiveDrivers() {
        return carpools.stream()
                .filter(CarPool::isActive)
                .map(CarPool::getDriver)
                .distinct()
                .collect(Collectors.toList());
    }
}