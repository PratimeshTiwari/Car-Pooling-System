import java.util.ArrayList;
import java.util.List;

public class CarPool {
    private String poolId;
    private Driver driver;
    private String source;
    private String destination;
    private String exactPickupLocation;
    private String exactDropLocation;
    private String dateTime;
    private int availableSeats;
    private double price;
    private List<Rider> riders;
    private List<Rider> pendingRequests;
    private boolean smokingAllowed;
    private boolean petsAllowed;
    private boolean musicAllowed;
    private boolean femaleOnly;
    private String additionalRules;
    private String status; // ACTIVE, COMPLETED, CANCELLED

    public CarPool(String poolId, Driver driver, String source, String destination,
                   String exactPickupLocation, String exactDropLocation,
                   String dateTime, int availableSeats, double price,
                   boolean smokingAllowed, boolean petsAllowed,
                   boolean musicAllowed, boolean femaleOnly,
                   String additionalRules) {
        this.poolId = poolId;
        this.driver = driver;
        this.source = source;
        this.destination = destination;
        this.exactPickupLocation = exactPickupLocation;
        this.exactDropLocation = exactDropLocation;
        this.dateTime = dateTime;
        this.availableSeats = availableSeats;
        this.price = price;
        this.smokingAllowed = smokingAllowed;
        this.petsAllowed = petsAllowed;
        this.musicAllowed = musicAllowed;
        this.femaleOnly = femaleOnly;
        this.additionalRules = additionalRules;
        this.riders = new ArrayList<>();
        this.pendingRequests = new ArrayList<>();
        this.status = "ACTIVE";
    }

    // Getters and setters
    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getExactPickupLocation() {
        return exactPickupLocation;
    }

    public void setExactPickupLocation(String exactPickupLocation) {
        this.exactPickupLocation = exactPickupLocation;
    }

    public String getExactDropLocation() {
        return exactDropLocation;
    }

    public void setExactDropLocation(String exactDropLocation) {
        this.exactDropLocation = exactDropLocation;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Rider> getRiders() {
        return riders;
    }

    public void setRiders(List<Rider> riders) {
        this.riders = riders;
    }

    public List<Rider> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(List<Rider> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public boolean isMusicAllowed() {
        return musicAllowed;
    }

    public void setMusicAllowed(boolean musicAllowed) {
        this.musicAllowed = musicAllowed;
    }

    public boolean isFemaleOnly() {
        return femaleOnly;
    }

    public void setFemaleOnly(boolean femaleOnly) {
        this.femaleOnly = femaleOnly;
    }

    public String getAdditionalRules() {
        return additionalRules;
    }

    public void setAdditionalRules(String additionalRules) {
        this.additionalRules = additionalRules;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Utility methods
    public boolean requestToJoin(Rider rider) {
        if (availableSeats > 0 && !pendingRequests.contains(rider) && !riders.contains(rider)) {
            pendingRequests.add(rider);
            return true;
        }
        return false;
    }

    public boolean approveRequest(Rider rider) {
        if (pendingRequests.contains(rider) && availableSeats > 0) {
            pendingRequests.remove(rider);
            riders.add(rider);
            availableSeats--;
            return true;
        }
        return false;
    }

    public boolean rejectRequest(Rider rider) {
        return pendingRequests.remove(rider);
    }

    public boolean isFull() {
        return availableSeats == 0;
    }

    public boolean isActive() {
        return status.equals("ACTIVE");
    }

    public void completeRide() {
        this.status = "COMPLETED";
    }

    public void cancelRide() {
        this.status = "CANCELLED";
    }

//    public List<Rider> getPendingRequests() {
//        return new ArrayList<>(pendingRequests); // Return a copy to prevent external modification
//    }

    @Override
    public String toString() {
        return "CarPool{" +
                "poolId='" + poolId + '\'' +
                ", driver=" + driver.getName() +
                ", source='" + source + '\'' +
                ", destination='" + destination + '\'' +
                ", exactPickupLocation='" + exactPickupLocation + '\'' +
                ", exactDropLocation='" + exactDropLocation + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", availableSeats=" + availableSeats +
                ", price=" + price +
                ", riders=" + riders.size() +
                ", pendingRequests=" + pendingRequests.size() +
                ", smokingAllowed=" + smokingAllowed +
                ", petsAllowed=" + petsAllowed +
                ", musicAllowed=" + musicAllowed +
                ", femaleOnly=" + femaleOnly +
                ", status='" + status + '\'' +
                '}';
    }
}