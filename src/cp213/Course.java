package cp213;

public abstract class Course {
    private String code;
    private String title;
    private double credits;

    // Course info constructor
    public Course(String code, String title, double credits) {
	this.code = code;
	this.title = title;
	this.credits = credits;
    }

    // GETTERS and toString with course type

    public String getCode() {
	return code;
    }

    public String getTitle() {
	return title;
    }

    public double getCredits() {
	return credits;
    }

    public abstract String getType();

    @Override
    public String toString() {
	return code + " - " + title + " (" + credits + " credits)" + " - " + getType();
    }
}