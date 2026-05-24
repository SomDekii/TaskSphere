package TaskSphere.demo.dto;

public class AuthResponse {

    private String token;
    private String userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public AuthResponse(String token, String userId, String username, String email) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public AuthResponse(String token, String username,
                        String firstName, String lastName, String email) {
        this.token     = token;
        this.username  = username;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
    }

    public String getToken()     { return token; }
    public String getUserId()    { return userId; }
    public String getUsername()  { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getEmail()     { return email; }
}
