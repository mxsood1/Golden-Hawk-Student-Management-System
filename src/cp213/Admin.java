package cp213;

public class Admin extends User {
    // Admin extends User and uses variables to create a user of "ADMIN" role
    // (getRole())
    public Admin(String id, String lastName, String firstName, String email, String password) {
	super(id, lastName, firstName, email, password);
    }

    @Override
    public String getRole() {
	return "ADMIN";
    }
}