package cp213;

public class Enrollment {
    private Student student;
    private Course course;
    private double grade;

    // Creates enrollment for User with default grade of -1
    public Enrollment(Student student, Course course) {
	this.student = student;
	this.course = course;
	this.grade = -1;
    }

    // GETTERS and SETTER for grade

    public Student getStudent() {
	return student;
    }

    public Course getCourse() {
	return course;
    }

    public double getGrade() {
	return grade;
    }

    public void setGrade(double grade) {
	this.grade = grade;
    }

    // Grabs grade of user and formats for toString
    // Student ID, Full Name, Course Code, Course Type, Grade
    @Override
    public String toString() {
	String gradeStr;
	if (grade < 0) {
	    gradeStr = "No grade";
	} else {
	    gradeStr = String.format("%.1f", grade);
	}
	return student.getId() + " - " + student.getLastName() + ", " + student.getFirstName() + " | "
		+ course.getCode() + " (" + course.getType() + ") | Grade: " + gradeStr;
    }
}