package TaskSphere.demo.dto;

import jakarta.validation.constraints.*;

public class SignupRequest {

    private String firstName;

    private String lastName;

    @NotBlank
    @Size(min = 3, max = 30)
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    public String getFirstName() { return firstName; }
    public String getLastName()  { return lastName; }
    public String getUsername()  { return username; }
    public String getEmail()     { return email; }
    public String getPassword()  { return password; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName)   { this.lastName = lastName; }
    public void setUsername(String username)   { this.username = username; }
    public void setEmail(String email)         { this.email = email; }
    public void setPassword(String password)   { this.password = password; }
}
