package cp213;

public class Lecture extends Course {
    // Lecture extends Course and uses variables to create a course of "Lecture" type
    public Lecture(String code, String title, double credits) {
	super(code, title, credits);
    }

    @Override
    public String getType() {
	return "Lecture";
    }
}