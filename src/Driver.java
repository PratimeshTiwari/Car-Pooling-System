public class Driver extends User {
    private String carModel;
    private String licenseNumber;
    private int seatsAvailable;

    public Driver(String userId, String name, String password, String email,
                  String carModel, String licenseNumber, int seatsAvailable) {
        super(userId, name, password, email);
        this.carModel = carModel;
        this.licenseNumber = licenseNumber;
        this.seatsAvailable = seatsAvailable;
    }

    public String getCarModel() { return carModel; }
    public String getLicenseNumber() { return licenseNumber; }
    public int getSeatsAvailable() { return seatsAvailable; }
}
