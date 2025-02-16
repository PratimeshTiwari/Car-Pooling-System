public class Rider extends User {
    private String preferredPickupLocation;

    public Rider(String userId, String name, String password, String email,
                 String preferredPickupLocation) {
        super(userId, name, password, email);
        this.preferredPickupLocation = preferredPickupLocation;
    }

    public String getPreferredPickupLocation() { return preferredPickupLocation; }
}
