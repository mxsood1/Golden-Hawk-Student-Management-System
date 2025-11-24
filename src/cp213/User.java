package cp213;

public abstract class User {
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;

    // Constructor for all info that Users possess
    public User(String id, String lastName, String firstName, String email, String password) {
	this.id = id;
	this.lastName = lastName;
	this.firstName = firstName;
	this.email = email;
	this.password = password;
    }

    // GETTERS and toString with user role

    public String getId() {
	return id;
    }

    public String getLastName() {
	return lastName;
    }

    public String getFirstName() {
	return firstName;
    }

    public String getEmail() {
	return email;
    }

    public String getPassword() {
	return password;
    }

    public abstract String getRole();

    @Override
    public String toString() {
	return id + " - " + lastName + ", " + firstName + " - " + getRole();
    }
}