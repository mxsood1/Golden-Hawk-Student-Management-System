package cp213;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GoldenHawkApp extends JFrame {
    // All users, students, courses, and enrollment lists
    private ArrayList<User> users;
    private ArrayList<Student> students;
    private ArrayList<Course> courses;
    private ArrayList<Enrollment> enrollments;
    // True if current session is admin, false if student
    private boolean isAdmin;
    // If logged in as student, this is the associated Student object
    private Student loggedInStudent;
    // Student tab
    private JTextField studentIdField, studentLastNameField, studentFirstNameField, studentEmailField,
	    studentPasswordField, studentSearchField;
    private DefaultListModel<Student> studentListModel;
    private JList<Student> studentList;
    private JButton addStudentButton, deleteStudentButton, searchStudentButton;
    // Course tab
    private JTextField courseCodeField, courseTitleField, courseCreditsField, courseSearchField;
    private JComboBox<String> courseTypeCombo;
    private DefaultListModel<Course> courseListModel;
    private JList<Course> courseList;
    private JButton addCourseButton, deleteCourseButton, searchCourseButton;
    // Enrollments tab
    private JComboBox<Student> enrollmentStudentCombo;
    private JComboBox<Course> enrollmentCourseCombo;
    private JTextField gradeField, enrollmentSearchField;

    private DefaultListModel<Enrollment> enrollmentListModel;
    private JList<Enrollment> enrollmentList;
    private JLabel gpaLabel, gradeLabel;
    private JButton enrollButton, setGradeButton, calcGpaButton, deleteEnrollmentButton, searchEnrollmentButton;

    public GoldenHawkApp(boolean isAdmin, String loggedInStudentId) {
	// Window title
	super("Golden Hawk Student Management System");
	this.isAdmin = isAdmin;
	// Load all users from file
	users = DataStorage.loadUsers("users.txt");
	// Build separate student list from users
	students = new ArrayList<>();
	for (User user : users) {
	    if (user instanceof Student) {
		students.add((Student) user);
	    }
	}
	// Load courses and enrollments from files
	courses = DataStorage.loadCourses("courses.txt");
	enrollments = DataStorage.loadEnrollments("enrollments.txt", users, courses);
	// Safety checks in case anything returned null
	if (students == null) {
	    students = new ArrayList<>();
	}
	if (courses == null) {
	    courses = new ArrayList<>();
	}
	if (enrollments == null) {
	    enrollments = new ArrayList<>();
	}

	// If this is a student login, find their Student object
	if (!isAdmin) {
	    // If there are no students, cannot continue
	    if (students.isEmpty()) {
		JOptionPane.showMessageDialog(this,
			"No students exist in the system. Please login as Admin first to create students.",
			"No Students", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	    }

	    else {
		this.loggedInStudent = null;
		// Match the student by ID from the login
		if (loggedInStudentId != null) {
		    for (Student student : students) {
			if (student.getId().equals(loggedInStudentId)) {
			    this.loggedInStudent = student;
			    break;
			}
		    }
		}
		// If no match was found, login error
		if (this.loggedInStudent == null) {
		    JOptionPane.showMessageDialog(this, "Could not find student with ID: " + loggedInStudentId,
			    "Login Error", JOptionPane.ERROR_MESSAGE);
		    System.exit(0);
		}
	    }
	}

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(950, 600);
	// Create tabbed pane
	JTabbedPane tabs = new JTabbedPane();
	// Admin gets Students and Courses tabs
	if (isAdmin) {
	    tabs.addTab("Students", createStudentPanel());
	    tabs.addTab("Courses", createCoursePanel());
	}
	// All users get Enrollments & Grades tab
	tabs.addTab("Enrollments & Grades", createEnrollmentPanel());
	// Add tabs to the frame
	setLayout(new BorderLayout());
	add(tabs, BorderLayout.CENTER);

	// Adjust title and restrictions based on role
	if (!isAdmin) {
	    setTitle("Golden Hawk Student Management System - Student View");
	    applyStudentRestrictions();
	} else {
	    setTitle("Golden Hawk Student Management System - Admin View");
	}
    }

    private JPanel createStudentPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	// Adding a new student
	JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));
	form.setBorder(BorderFactory.createTitledBorder("Add Student"));
	// Input fields and labels
	form.add(new JLabel("Student ID:"));
	studentIdField = new JTextField(15);
	form.add(studentIdField);

	form.add(new JLabel("First Name:"));
	studentFirstNameField = new JTextField(15);
	form.add(studentFirstNameField);

	form.add(new JLabel("Last Name:"));
	studentLastNameField = new JTextField(15);
	form.add(studentLastNameField);

	form.add(new JLabel("Email:"));
	studentEmailField = new JTextField(15);
	form.add(studentEmailField);

	form.add(new JLabel("Password:"));
	studentPasswordField = new JTextField(15);
	form.add(studentPasswordField);
	// Button row
	form.add(new JLabel(""));
	addStudentButton = new JButton("Add Student");
	form.add(addStudentButton);

	panel.add(form, BorderLayout.NORTH);
	// Center list displaying all students
	studentListModel = new DefaultListModel<>();
	for (Student student : students) {
	    studentListModel.addElement(student);
	}

	studentList = new JList<>(studentListModel);
	JScrollPane scroll = new JScrollPane(studentList);
	scroll.setBorder(BorderFactory.createTitledBorder("Students"));
	panel.add(scroll, BorderLayout.CENTER);
	// Bottom panel buttons
	JPanel bottom = new JPanel(new BorderLayout());
	bottom.setBorder(BorderFactory.createTitledBorder("Search / Delete"));
	// Left side buttons
	JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
	studentSearchField = new JTextField(15);
	searchStudentButton = new JButton("Search");
	deleteStudentButton = new JButton("Delete Selected Student");

	left.add(new JLabel("Search by ID or Name:"));
	left.add(studentSearchField);
	left.add(searchStudentButton);
	left.add(deleteStudentButton);
	// Right side buttons
	JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JButton switchButton = new JButton("Switch Account");
	JButton exitButton = new JButton("Exit");
	right.add(switchButton);
	right.add(exitButton);

	bottom.add(left, BorderLayout.WEST);
	bottom.add(right, BorderLayout.EAST);
	panel.add(bottom, BorderLayout.SOUTH);
	// Connect button actions
	addStudentButton.addActionListener(e -> addStudent());
	searchStudentButton.addActionListener(e -> searchStudent());
	deleteStudentButton.addActionListener(e -> deleteSelectedStudent());
	configureSwitchExit(switchButton, exitButton);
	return panel;
    }

    private JPanel createCoursePanel() {
	JPanel panel = new JPanel(new BorderLayout());
	// Add a new course
	JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
	form.setBorder(BorderFactory.createTitledBorder("Add Course"));
	// Text fields
	courseCodeField = new JTextField();
	courseTitleField = new JTextField();
	courseCreditsField = new JTextField();
	courseTypeCombo = new JComboBox<>(new String[] { "Lecture", "Lab", "Tutorial" });
	addCourseButton = new JButton("Add Course");
	// Labels and fields
	form.add(new JLabel("Course Code:"));
	form.add(courseCodeField);
	form.add(new JLabel("Title:"));
	form.add(courseTitleField);
	form.add(new JLabel("Credits:"));
	form.add(courseCreditsField);
	form.add(new JLabel("Type:"));
	form.add(courseTypeCombo);
	form.add(new JLabel(""));
	form.add(addCourseButton);
	// Admin sees add course form
	if (isAdmin) {
	    panel.add(form, BorderLayout.NORTH);
	}
	// Course list in the middle
	courseListModel = new DefaultListModel<>();

	if (isAdmin) {
	    // Admin sees all courses
	    for (Course c : courses) {
		courseListModel.addElement(c);
	    }
	} else {
	    // Students see only courses they are enrolled in
	    ArrayList<Course> myCourses = new ArrayList<>();
	    for (Enrollment e : enrollments) {
		if (sameStudent(e.getStudent(), loggedInStudent)) {
		    Course c = e.getCourse();
		    boolean already = false;

		    for (Course existing : myCourses) {
			if (sameCourse(existing, c)) {
			    already = true;
			    break;
			}
		    }

		    if (!already) {
			myCourses.add(c);
			courseListModel.addElement(c);
		    }
		}
	    }
	}

	courseList = new JList<>(courseListModel);
	JScrollPane scroll = new JScrollPane(courseList);
	scroll.setBorder(BorderFactory.createTitledBorder("Courses"));
	panel.add(scroll, BorderLayout.CENTER);
	// Bottom part
	JPanel bottom = new JPanel(new BorderLayout());
	bottom.setBorder(BorderFactory.createTitledBorder("Search / Delete"));
	// Left side
	JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
	courseSearchField = new JTextField(15);
	searchCourseButton = new JButton("Search");
	deleteCourseButton = new JButton("Delete Selected Course");

	left.add(new JLabel("Search by Code or Title:"));
	left.add(courseSearchField);
	left.add(searchCourseButton);
	left.add(deleteCourseButton);
	// Right side
	JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JButton switchButton = new JButton("Switch Account");
	JButton exitButton = new JButton("Exit");
	right.add(switchButton);
	right.add(exitButton);

	bottom.add(left, BorderLayout.WEST);
	bottom.add(right, BorderLayout.EAST);
	panel.add(bottom, BorderLayout.SOUTH);
	// Connect button actions
	addCourseButton.addActionListener(e -> addCourse());
	searchCourseButton.addActionListener(e -> searchCourse());
	deleteCourseButton.addActionListener(e -> deleteSelectedCourse());
	configureSwitchExit(switchButton, exitButton);
	return panel;
    }

    private JPanel createEnrollmentPanel() {
	JPanel panel = new JPanel(new BorderLayout());
	// Adding enrolling and grading
	JPanel form = new JPanel();
	form.setBorder(BorderFactory.createTitledBorder("Enroll / Grade"));
	// If Admin, include grade row
	int rows = 3;
	if (isAdmin) {
	    rows = 4;
	}

	form.setLayout(new GridLayout(rows, 1, 5, 5));
	// Student selection row
	JPanel studentRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
	studentRow.add(new JLabel("Student:"));
	enrollmentStudentCombo = new JComboBox<>();
	refreshStudentCombo();
	studentRow.add(enrollmentStudentCombo);
	form.add(studentRow);
	// Course selection and enroll row
	JPanel courseRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
	courseRow.add(new JLabel("Course:"));
	enrollmentCourseCombo = new JComboBox<>();
	refreshCourseCombo();
	courseRow.add(enrollmentCourseCombo);
	enrollButton = new JButton("Enroll");
	courseRow.add(enrollButton);
	form.add(courseRow);
	// Grading row (Admin only)
	if (isAdmin) {
	    JPanel gradeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
	    gradeLabel = new JLabel("Grade (0.0-100.0):");
	    gradeRow.add(gradeLabel);
	    gradeField = new JTextField(8);
	    gradeRow.add(gradeField);
	    setGradeButton = new JButton("Set Grade");
	    gradeRow.add(setGradeButton);
	    form.add(gradeRow);
	}
	// GPA display row
	JPanel gpaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
	gpaLabel = new JLabel("GPA: N/A");
	gpaRow.add(gpaLabel);
	calcGpaButton = new JButton("Calculate GPA");
	gpaRow.add(calcGpaButton);
	form.add(gpaRow);

	panel.add(form, BorderLayout.NORTH);
	// List of enrollments
	enrollmentListModel = new DefaultListModel<>();
	if (isAdmin) {
	    // Admin sees all students enrollments
	    for (Enrollment e : enrollments) {
		enrollmentListModel.addElement(e);
	    }
	} else {
	    // Students only see their own enrollments
	    for (Enrollment e : enrollments) {
		if (sameStudent(e.getStudent(), loggedInStudent)) {
		    enrollmentListModel.addElement(e);
		}
	    }
	}

	enrollmentList = new JList<>(enrollmentListModel);
	JScrollPane scroll = new JScrollPane(enrollmentList);
	scroll.setBorder(BorderFactory.createTitledBorder("Enrollments"));
	panel.add(scroll, BorderLayout.CENTER);
	// Bottom part
	JPanel bottom = new JPanel(new BorderLayout());
	bottom.setBorder(BorderFactory.createTitledBorder("Search / Delete"));
	// Left side
	JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
	enrollmentSearchField = new JTextField(20);
	searchEnrollmentButton = new JButton("Search");
	deleteEnrollmentButton = new JButton("Unenroll");

	left.add(new JLabel("Search by Student ID, Name, or Course Code:"));
	left.add(enrollmentSearchField);
	left.add(searchEnrollmentButton);
	left.add(deleteEnrollmentButton);
	// Right side
	JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	JButton switchButton = new JButton("Switch Account");
	JButton exitButton = new JButton("Exit");
	right.add(switchButton);
	right.add(exitButton);

	bottom.add(left, BorderLayout.WEST);
	bottom.add(right, BorderLayout.EAST);
	panel.add(bottom, BorderLayout.SOUTH);
	// Connect button actions
	enrollButton.addActionListener(e -> enrollStudent());
	if (setGradeButton != null) {
	    setGradeButton.addActionListener(e -> setGradeForSelectedEnrollment());
	}
	calcGpaButton.addActionListener(e -> calculateSelectedStudentGPA());
	deleteEnrollmentButton.addActionListener(e -> deleteSelectedEnrollment());
	searchEnrollmentButton.addActionListener(e -> searchEnrollment());
	configureSwitchExit(switchButton, exitButton);
	return panel;
    }

    private void addStudent() {
	String id = studentIdField.getText().trim();
	String firstName = studentFirstNameField.getText().trim();
	String lastName = studentLastNameField.getText().trim();
	String email = studentEmailField.getText().trim();
	String password = studentPasswordField.getText().trim();
	// Ensure all fields have data
	if (id.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Ensure email format is valid
	if (!isValidEmail(email)) {
	    JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Input Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Ensure Student ID format is valid
	if (!isValidStudentId(id)) {
	    JOptionPane.showMessageDialog(this, "Student ID must be exactly 9 digits.", "Input Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Check for duplicate ID or email among existing users
	for (User user : users) {
	    if (user.getId().equals(id)) {
		JOptionPane.showMessageDialog(this, "A user with this ID already exists.", "Input Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	    if (user.getEmail().equalsIgnoreCase(email)) {
		JOptionPane.showMessageDialog(this, "A user with this email already exists.", "Input Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	// Create new Student and add it
	Student student = new Student(id, lastName, firstName, email, password);
	students.add(student);
	users.add(student);

	if (studentListModel != null) {
	    studentListModel.addElement(student);
	}
	refreshStudentCombo();
	// Clear fields
	studentIdField.setText("");
	studentFirstNameField.setText("");
	studentLastNameField.setText("");
	studentEmailField.setText("");
	studentPasswordField.setText("");

	JOptionPane.showMessageDialog(this, "Student added (will be saved on exit).");
    }

    private void searchStudent() {
	String query = studentSearchField.getText().trim().toLowerCase();
	// Make sure search field isn't empty
	if (query.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Enter a search term (ID or name).", "Search",
		    JOptionPane.INFORMATION_MESSAGE);
	    return;
	}
	// Search through students
	for (int i = 0; i < studentListModel.size(); i++) {
	    Student student = studentListModel.getElementAt(i);
	    String id = student.getId().toLowerCase();
	    String name = student.getFirstName().toLowerCase() + " " + student.getLastName().toLowerCase();
	    // Match if query appears in ID or name
	    if (id.contains(query) || name.contains(query)) {
		studentList.setSelectedIndex(i);
		studentList.ensureIndexIsVisible(i);
		return;
	    }
	}

	JOptionPane.showMessageDialog(this, "No student found for: " + query, "Search",
		JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedStudent() {
	Student selected = studentList.getSelectedValue();
	// If nothing selected
	if (selected == null) {
	    JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Delete Student",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Confirm delete
	int confirm = JOptionPane.showConfirmDialog(this, "Delete student and all their enrollments?", "Confirm Delete",
		JOptionPane.YES_NO_OPTION);
	if (confirm != JOptionPane.YES_OPTION) {
	    return;
	}
	// Remove any enrollments associated with this students
	ArrayList<Enrollment> toRemove = new ArrayList<>();
	for (Enrollment enrollment : enrollments) {
	    if (sameStudent(enrollment.getStudent(), selected)) {
		toRemove.add(enrollment);
	    }
	}

	for (Enrollment enrollment : toRemove) {
	    enrollments.remove(enrollment);
	    enrollmentListModel.removeElement(enrollment);
	    enrollment.getStudent().removeEnrollment(enrollment);
	}
	// Remove student from users and UI
	users.remove(selected);
	studentListModel.removeElement(selected);
	refreshStudentCombo();

	JOptionPane.showMessageDialog(this,
		"Student deleted from interface. Changes can be saved to file if you choose to do so when exiting the application.",
		"Delete Student", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addCourse() {
	String code = courseCodeField.getText().trim();
	String title = courseTitleField.getText().trim();
	String creditsStr = courseCreditsField.getText().trim();
	String type = (String) courseTypeCombo.getSelectedItem();
	// Field validation
	if (code.isEmpty() || title.isEmpty() || creditsStr.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Parse credits
	double credits;
	try {
	    credits = Double.parseDouble(creditsStr);
	    if (credits < 0) {
		throw new NumberFormatException("Credits must be greater than or equal to 0");
	    }
	}

	catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this, "Credits must be greater than or equal to 0.", "Input Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Ensure course code is unique
	for (Course existing : courses) {
	    if (existing.getCode().equals(code)) {
		JOptionPane.showMessageDialog(this, "A course with this code already exists.", "Input Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	// Prevent exact duplicates in title/credits/type
	for (Course existing : courses) {
	    if (existing.getTitle().equalsIgnoreCase(title) && existing.getCredits() == credits
		    && existing.getType().equalsIgnoreCase(type)) {
		JOptionPane.showMessageDialog(this, "This course already exists.", "Input Error",
			JOptionPane.ERROR_MESSAGE);
		return;
	    }
	}
	// Create appropriate subclass of Course
	Course course;
	if ("Lecture".equals(type)) {
	    course = new Lecture(code, title, credits);
	} else if ("Lab".equals(type)) {
	    course = new Lab(code, title, credits);
	} else {
	    course = new Tutorial(code, title, credits);
	}
	// Add to courses and update UI
	courses.add(course);
	if (isAdmin && courseListModel != null) {
	    courseListModel.addElement(course);
	}
	refreshCourseCombo();
	// Clear input fields
	courseCodeField.setText("");
	courseTitleField.setText("");
	courseCreditsField.setText("");
    }

    private void searchCourse() {
	String query = courseSearchField.getText().trim().toLowerCase();
	// Check that field isn't empty
	if (query.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Enter a search term (code or title).", "Search",
		    JOptionPane.INFORMATION_MESSAGE);
	    return;
	}
	// Search through courses
	for (int i = 0; i < courseListModel.size(); i++) {
	    // Get info of course
	    Course c = courseListModel.getElementAt(i);
	    String code = c.getCode().toLowerCase();
	    String title = c.getTitle().toLowerCase();
	    // Return if course is found in course list
	    if (code.contains(query) || title.contains(query)) {
		courseList.setSelectedIndex(i);
		courseList.ensureIndexIsVisible(i);
		return;
	    }
	}
	JOptionPane.showMessageDialog(this, "No course found for: " + query, "Search", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSelectedCourse() {
	Course selected = courseList.getSelectedValue();
	// Check if any course is selected
	if (selected == null) {
	    JOptionPane.showMessageDialog(this, "Please select a course to delete.", "Delete Course",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Confirm dialog
	int confirm = JOptionPane.showConfirmDialog(this, "Delete course and all related enrollments?",
		"Confirm Delete", JOptionPane.YES_NO_OPTION);
	if (confirm != JOptionPane.YES_OPTION) {
	    return;
	}
	// Remove enrollments for this course
	ArrayList<Enrollment> toRemove = new ArrayList<>();
	for (Enrollment enrollment : enrollments) {
	    if (sameCourse(enrollment.getCourse(), selected)) {
		toRemove.add(enrollment);
	    }
	}

	for (Enrollment enrollment : toRemove) {
	    enrollments.remove(enrollment);
	    enrollmentListModel.removeElement(enrollment);
	    enrollment.getStudent().removeEnrollment(enrollment);
	}
	// Remove course from list
	courses.remove(selected);
	courseListModel.removeElement(selected);
	refreshCourseCombo();

	JOptionPane.showMessageDialog(this,
		"Course deleted from interface. Changes can be saved to file if you choose to do so when exiting the application.",
		"Delete Course", JOptionPane.INFORMATION_MESSAGE);
    }

    private void enrollStudent() {
	Student student = (Student) enrollmentStudentCombo.getSelectedItem();
	Course course = (Course) enrollmentCourseCombo.getSelectedItem();
	// Check if student and course both exist
	if (student == null || course == null) {
	    JOptionPane.showMessageDialog(this, "Please select a student and a course.", "Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Prevent duplicate enrollment of same student in same course
	for (Enrollment enrollment : enrollments) {
	    if (sameStudent(enrollment.getStudent(), student) && sameCourse(enrollment.getCourse(), course)) {
		JOptionPane.showMessageDialog(this, "This student is already enrolled in the selected course.",
			"Duplicate Enrollment", JOptionPane.WARNING_MESSAGE);
		return;
	    }
	}
	// Create and register new enrollment
	Enrollment enrollment = new Enrollment(student, course);
	enrollments.add(enrollment);
	student.addEnrollment(enrollment);
	// Show in list of Admin and to the logged in student
	if (isAdmin || sameStudent(student, loggedInStudent)) {
	    enrollmentListModel.addElement(enrollment);
	}
	// Ensure the course appears in the student view
	if (!isAdmin && sameStudent(student, loggedInStudent)) {
	    boolean exists = false;
	    for (int i = 0; i < courseListModel.size(); i++) {
		if (sameCourse(courseListModel.getElementAt(i), course)) {
		    exists = true;
		    break;
		}
	    }
	    if (!exists) {
		courseListModel.addElement(course);
	    }
	}
    }

    private void setGradeForSelectedEnrollment() {
	Enrollment selected = enrollmentList.getSelectedValue();
	// Check if any enrollment selected
	if (selected == null) {
	    JOptionPane.showMessageDialog(this, "Please select an enrollment from the list.", "Error",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Check if grade field has input
	String gradeStr = gradeField.getText().trim();
	if (gradeStr.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Please enter a grade.", "Input Error", JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Validate input and update selected enrollment
	try {
	    double grade = Double.parseDouble(gradeStr);
	    if (grade < 0 || grade > 100) {
		throw new IllegalArgumentException("Grade must be between 0 and 100.");
	    }
	    selected.setGrade(grade);
	    enrollmentList.repaint();
	} catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this, "Grade must be a valid number.", "Input Error",
		    JOptionPane.ERROR_MESSAGE);
	} catch (IllegalArgumentException ex) {
	    JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
	}
    }

    private void calculateSelectedStudentGPA() {
	Student selectedStudent;
	// Students always use their own account
	if (!isAdmin && loggedInStudent != null) {
	    selectedStudent = loggedInStudent;
	} else {
	    // Admin chooses from whichever selected
	    selectedStudent = (Student) enrollmentStudentCombo.getSelectedItem();
	}
	// If no student selected, return error
	if (selectedStudent == null) {
	    JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Display GPA
	double gpa = selectedStudent.calculateGPA();
	gpaLabel.setText(String.format("GPA: %.2f", gpa));
    }

    private void deleteSelectedEnrollment() {
	Enrollment selected = enrollmentList.getSelectedValue();
	// Check if any enrollment selected
	if (selected == null) {
	    JOptionPane.showMessageDialog(this, "Please select an enrollment to delete.", "Delete Enrollment",
		    JOptionPane.ERROR_MESSAGE);
	    return;
	}
	// Confirm delete
	int confirm = JOptionPane.showConfirmDialog(this, "Delete this enrollment?", "Confirm Delete",
		JOptionPane.YES_NO_OPTION);
	if (confirm != JOptionPane.YES_OPTION) {
	    return;
	}
	// Remove enrollment from list and UI
	enrollments.remove(selected);
	enrollmentListModel.removeElement(selected);
	selected.getStudent().removeEnrollment(selected);

	JOptionPane.showMessageDialog(this,
		"Enrollment deleted from interface. Changes can be saved to file if you choose to do so when exiting the application.",
		"Delete Enrollment", JOptionPane.INFORMATION_MESSAGE);
    }

    private void searchEnrollment() {
	String query = enrollmentSearchField.getText().trim().toLowerCase();
	// Check if search field is empty
	if (query.isEmpty()) {
	    JOptionPane.showMessageDialog(this, "Enter a search term.", "Search", JOptionPane.INFORMATION_MESSAGE);
	    return;
	}
	// Search through enrollments list
	for (int i = 0; i < enrollmentListModel.size(); i++) {
	    // Get enrollment info
	    Enrollment e = enrollmentListModel.getElementAt(i);
	    String sid = e.getStudent().getId().toLowerCase();
	    String sname = e.getStudent().getLastName().toLowerCase() + ", "
		    + e.getStudent().getFirstName().toLowerCase();
	    String ccode = e.getCourse().getCode().toLowerCase();
	    // If search matches any enrollment, return that enrollment
	    if (sid.contains(query) || sname.contains(query) || ccode.contains(query)) {
		enrollmentList.setSelectedIndex(i);
		enrollmentList.ensureIndexIsVisible(i);
		return;
	    }
	}

	JOptionPane.showMessageDialog(this, "No enrollment found for: " + query, "Search",
		JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshStudentCombo() {
	// Refreshes
	if (enrollmentStudentCombo == null) {
	    return;
	}
	enrollmentStudentCombo.removeAllItems();
	// If not Admin, add logged in student to combo
	if (!isAdmin) {
	    if (loggedInStudent != null) {
		enrollmentStudentCombo.addItem(loggedInStudent);
	    }
	} else {
	    for (Student s : students) {
		enrollmentStudentCombo.addItem(s);
	    }
	}
    }

    private void refreshCourseCombo() {
	// Refreshes course combo
	if (enrollmentCourseCombo == null) {
	    return;
	}
	enrollmentCourseCombo.removeAllItems();
	for (Course c : courses) {
	    enrollmentCourseCombo.addItem(c);
	}
    }

    private void applyStudentRestrictions() {
	// Hide all buttons/fields that a student should not use
	if (addStudentButton != null) {
	    addStudentButton.setEnabled(false);
	    addStudentButton.setVisible(false);
	}
	if (deleteStudentButton != null) {
	    deleteStudentButton.setEnabled(false);
	    deleteStudentButton.setVisible(false);
	}
	if (addCourseButton != null) {
	    addCourseButton.setEnabled(false);
	    addCourseButton.setVisible(false);
	}
	if (deleteCourseButton != null) {
	    deleteCourseButton.setEnabled(false);
	    deleteCourseButton.setVisible(false);
	}
	if (setGradeButton != null) {
	    setGradeButton.setEnabled(false);
	    setGradeButton.setVisible(false);
	}
	if (gradeField != null) {
	    gradeField.setEnabled(false);
	    gradeField.setVisible(false);
	}
	if (gradeLabel != null) {
	    gradeLabel.setEnabled(false);
	    gradeLabel.setVisible(false);
	}
    }

    private boolean sameStudent(Student a, Student b) {
	// Checks if students exist
	if (a == null || b == null) {
	    return false;
	}
	// Compares students
	return a.getId().equals(b.getId());
    }

    private boolean sameCourse(Course a, Course b) {
	// Checks if courses exist
	if (a == null || b == null) {
	    return false;
	}
	// Compares courses
	return a.getCode().equals(b.getCode());
    }

    private void saveAll() {
	// Saves all new updates to users, courses, and enrollments to their respective
	// text files
	DataStorage.saveUsers(users, "users.txt");
	DataStorage.saveCourses(courses, "courses.txt");
	DataStorage.saveEnrollments(enrollments, "enrollments.txt");
    }

    private void configureSwitchExit(JButton switchButton, JButton exitButton) {
	// Ask to save on click, then quit
	exitButton.addActionListener(e -> {
	    int choice = JOptionPane.showConfirmDialog(this, "Do you want to save before exiting?", "Save Changes",
		    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	    // Cancel will return to program
	    if (choice == JOptionPane.CANCEL_OPTION) {
		return;
	    }
	    if (choice == JOptionPane.YES_OPTION) {
		saveAll();
	    }
	    System.exit(0);
	});

	// Ask to save on click, then go back to log in
	switchButton.addActionListener(e -> {
	    int choice = JOptionPane.showConfirmDialog(this, "Do you want to save before switching accounts?",
		    "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
	    // Cancel will return to program
	    if (choice == JOptionPane.CANCEL_OPTION) {
		return;
	    }
	    if (choice == JOptionPane.YES_OPTION) {
		saveAll();
	    }
	    // Close current window
	    dispose();
	    // Show login window again on screen
	    SwingUtilities.invokeLater(() -> {
		ArrayList<User> users = DataStorage.loadUsers("users.txt");
		Login login = new Login(null, users);
		login.setVisible(true);
		// Close if no login
		if (!login.isSucceeded()) {
		    System.exit(0);
		}
		// Assign role upon login
		User user = login.getLoggedInUser();
		boolean newIsAdmin = "ADMIN".equalsIgnoreCase(user.getRole());
		String newStudentId = "STUDENT".equalsIgnoreCase(user.getRole()) ? user.getId() : null;
		// Launch app with selected login and role
		GoldenHawkApp app = new GoldenHawkApp(newIsAdmin, newStudentId);
		app.setVisible(true);
	    });
	});
    }

    private boolean isValidEmail(String email) {
	// This whole code is to validate if an email input matches the Laurier format
	if (email.length() != 21) {
	    return false;
	}

	for (int i = 0; i < 3; i++) {
	    if (!Character.isLetter(email.charAt(i))) {
		return false;
	    }
	}

	for (int i = 3; i < 7; i++) {
	    if (!Character.isDigit(email.charAt(i))) {
		return false;
	    }
	}

	if (email.charAt(7) != '@') {
	    return false;
	}

	String domain = email.substring(8);
	if (!domain.equals("mylaurier.ca")) {
	    return false;
	}

	return true;
    }

    private boolean isValidStudentId(String id) {
	// Checks if student ID is input in Laurier format
	if (id.length() != 9) {
	    return false;
	}

	for (int i = 0; i < id.length(); i++) {
	    if (!Character.isDigit(id.charAt(i))) {
		return false;
	    }
	}
	return true;
    }

    public static void main(String[] args) {
	ArrayList<User> users = DataStorage.loadUsers("users.txt");

	SwingUtilities.invokeLater(() -> {

	    UIDesign.apply();

	    Login login = new Login(null, users);
	    login.setVisible(true);

	    if (!login.isSucceeded()) {
		System.exit(0);
	    }

	    User user = login.getLoggedInUser();
	    boolean isAdmin = "ADMIN".equalsIgnoreCase(user.getRole());

	    String studentId = null;
	    if ("STUDENT".equalsIgnoreCase(user.getRole())) {
		studentId = user.getId();
	    }

	    GoldenHawkApp app = new GoldenHawkApp(isAdmin, studentId);
	    app.setVisible(true);
	});
    }
}