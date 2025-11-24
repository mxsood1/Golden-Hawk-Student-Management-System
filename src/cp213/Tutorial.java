package cp213;

public class Tutorial extends Course {
    // Tutorial extends Course and uses variables to create a course of "Tutorial" type
    public Tutorial(String code, String title, double credits) {
	super(code, title, credits);
    }

    @Override
    public String getType() {
	return "Tutorial";
    }
}