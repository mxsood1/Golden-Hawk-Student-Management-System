package cp213;

public class Lab extends Course {
    // Lab extends Course and uses variables to create a course of "Lab" type
    public Lab(String code, String title, double credits) {
	super(code, title, credits);
    }

    @Override
    public String getType() {
	return "Lab";
    }
}