package cp213;

import java.util.ArrayList;

public class Student extends User {
    // Variables to categorize GPA groups
    public static final int CEILING_TWELVE = 100;
    public static final int FLOOR_TWELVE = 90;

    public static final int CEILING_ELEVEN = 89;
    public static final int FLOOR_ELEVEN = 85;

    public static final int CEILING_TEN = 84;
    public static final int FLOOR_TEN = 80;

    public static final int CEILING_NINE = 79;
    public static final int FLOOR_NINE = 77;

    public static final int CEILING_EIGHT = 76;
    public static final int FLOOR_EIGHT = 73;

    public static final int CEILING_SEVEN = 72;
    public static final int FLOOR_SEVEN = 70;

    public static final int CEILING_SIX = 69;
    public static final int FLOOR_SIX = 67;

    public static final int CEILING_FIVE = 66;
    public static final int FLOOR_FIVE = 63;

    public static final int CEILING_FOUR = 62;
    public static final int FLOOR_FOUR = 60;

    public static final int CEILING_THREE = 59;
    public static final int FLOOR_THREE = 57;

    public static final int CEILING_TWO = 56;
    public static final int FLOOR_TWO = 53;

    public static final int CEILING_ONE = 52;
    public static final int FLOOR_ONE = 50;

    // Hosts all enrollments for a given user/student
    private ArrayList<Enrollment> enrollments = new ArrayList<>();

    // Student extends User and uses variables to create a user of "STUDENT" role
    // (getRole())
    public Student(String id, String lastName, String firstName, String email, String password) {
	super(id, lastName, firstName, email, password);
    }

    public ArrayList<Enrollment> getEnrollments() {
	return enrollments;
    }

    // Checks for duplicate enrollment and enrolls
    public void addEnrollment(Enrollment enrollment) {
	if (!enrollments.contains(enrollment)) {
	    enrollments.add(enrollment);
	}
    }

    // Removes enrollment for the user
    public void removeEnrollment(Enrollment enrollment) {
	enrollments.remove(enrollment);
    }

    // GPA Calculations
    public double calculateGPA() {
	int totalPoints = 0;
	double totalCredits = 0.0;

	// If no enrollments, GPA is 0
	if (enrollments.isEmpty()) {
	    return 0.0;
	}

	// Loops through enrollments of a user
	for (Enrollment enrollment : enrollments) {
	    // Checks if enrollment contains a grade
	    if (enrollment.getGrade() < 0) {
		continue;
	    }

	    int points;
	    double grade = enrollment.getGrade();

	    // Grabs the points equivalent to the grade group
	    if (grade >= FLOOR_TWELVE && grade <= CEILING_TWELVE) {
		points = 12;
	    } else if (grade >= FLOOR_ELEVEN && grade <= CEILING_ELEVEN) {
		points = 11;
	    } else if (grade >= FLOOR_TEN && grade <= CEILING_TEN) {
		points = 10;
	    } else if (grade >= FLOOR_NINE && grade <= CEILING_NINE) {
		points = 9;
	    } else if (grade >= FLOOR_EIGHT && grade <= CEILING_EIGHT) {
		points = 8;
	    } else if (grade >= FLOOR_SEVEN && grade <= CEILING_SEVEN) {
		points = 7;
	    } else if (grade >= FLOOR_SIX && grade <= CEILING_SIX) {
		points = 6;
	    } else if (grade >= FLOOR_FIVE && grade <= CEILING_FIVE) {
		points = 5;
	    } else if (grade >= FLOOR_FOUR && grade <= CEILING_FOUR) {
		points = 4;
	    } else if (grade >= FLOOR_THREE && grade <= CEILING_THREE) {
		points = 3;
	    } else if (grade >= FLOOR_TWO && grade <= CEILING_TWO) {
		points = 2;
	    } else if (grade >= FLOOR_ONE && grade <= CEILING_ONE) {
		points = 1;
	    } else {
		points = 0;
	    }

	    double credits = enrollment.getCourse().getCredits();
	    totalPoints += points * credits;
	    totalCredits += credits;
	}

	if (totalCredits == 0) {
	    return 0.0;
	}
	// Calculates total GPA
	return totalPoints / totalCredits;
    }

    @Override
    public String getRole() {
	return "STUDENT";
    }
}