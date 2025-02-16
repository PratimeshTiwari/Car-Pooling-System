// User.java (Base class for Driver and Rider)
public class User {
    private String userId;
    private String name;
    private String password;
    private String email;

    public User(String userId, String name, String password, String email) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
}