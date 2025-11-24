package cp213;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataStorage {
    // Integers to determine indexing for each class
    public static final int PARTS_FOR_USERS = 6;
    public static final int PARTS_FOR_COURSES = 4;
    public static final int PARTS_FOR_ENROLLMENTS = 3;

    public static ArrayList<User> loadUsers(String filename) {
	// Grab users from .txt and creates ArrayList
	ArrayList<User> userList = new ArrayList<>();

	File file = new File(filename);
	if (!file.exists()) {
	    return userList;
	}

	// Scan through each line in file
	try (Scanner scanner = new Scanner(file)) {
	    while (scanner.hasNextLine()) {
		// Skip empty or examples lines
		String line = scanner.nextLine().trim();
		if (line.isEmpty() || line.startsWith("#")) {
		    continue;
		}

		// Grab all user info by splitting into parts
		String[] parts = line.split(";");
		if (parts.length < PARTS_FOR_USERS) {
		    continue;
		}

		// Assign appropriate variables to each part
		String id = parts[0].trim();
		String lastName = parts[1].trim();
		String firstName = parts[2].trim();
		String email = parts[3].trim();
		String password = parts[4].trim();
		String role = parts[5].trim().toUpperCase();

		// Assign roles
		if (role.equals("ADMIN")) {
		    userList.add(new Admin(id, lastName, firstName, email, password));
		} else if (role.equals("STUDENT")) {
		    userList.add(new Student(id, lastName, firstName, email, password));
		}
	    }
	} catch (Exception e) {
	    System.err.println("Error reading users from " + filename + ": " + e.getMessage());
	}
	// Sort and return users
	sortUsers(userList);
	return userList;
    }

    public static ArrayList<Course> loadCourses(String filename) {
	// Grab courses from .txt and creates ArrayList
	ArrayList<Course> courseList = new ArrayList<>();
	File file = new File(filename);
	if (!file.exists()) {
	    return courseList;
	}

	// Scan through each line in file
	try (Scanner scanner = new Scanner(file)) {
	    while (scanner.hasNextLine()) {
		// Skip empty or examples lines
		String line = scanner.nextLine().trim();
		if (line.isEmpty() || line.startsWith("#")) {
		    continue;
		}

		// Grab all course info by splitting into parts
		String[] parts = line.split(";");
		if (parts.length < PARTS_FOR_COURSES) {
		    continue;
		}

		// Assign appropriate variables to each part
		String code = parts[0].trim();
		String title = parts[1].trim();

		// Parse course credit
		double credits;
		try {
		    credits = Double.parseDouble(parts[2].trim());
		} catch (Exception e) {
		    continue;
		}

		String type = parts[3].trim();

		// Assign course type and add to array list
		Course course;
		if (type.equalsIgnoreCase("Lecture")) {
		    course = new Lecture(code, title, credits);
		} else if (type.equalsIgnoreCase("Lab")) {
		    course = new Lab(code, title, credits);
		} else {
		    course = new Tutorial(code, title, credits);
		}
		courseList.add(course);
	    }
	} catch (Exception e) {
	    System.err.println("Error reading courses from " + filename + ": " + e.getMessage());
	}
	// Sort and return courses
	sortCourses(courseList);
	return courseList;
    }

    public static ArrayList<Enrollment> loadEnrollments(String filename, ArrayList<User> users,
	    ArrayList<Course> courses) {
	// Grab enrollments from .txt and creates ArrayList
	ArrayList<Enrollment> enrollmentList = new ArrayList<>();
	File file = new File(filename);
	if (!file.exists()) {
	    return enrollmentList;
	}

	// Scan through each line in file
	try (Scanner scanner = new Scanner(file)) {
	    while (scanner.hasNextLine()) {
		// Skip empty or examples lines
		String line = scanner.nextLine().trim();
		if (line.isEmpty() || line.startsWith("#")) {
		    continue;
		}

		// Grab all enrollment info by splitting into parts
		String[] parts = line.split(";");
		if (parts.length < PARTS_FOR_ENROLLMENTS) {
		    continue;
		}

		// Assign appropriate variables to each part
		String studentID = parts[0].trim();
		String courseCode = parts[1].trim();

		// Search for existing student and/or course
		Student student = findStudentById(users, studentID);
		Course course = findCourseByCode(courses, courseCode);
		if (student == null || course == null) {
		    continue;
		}

		// Parse grade
		double grade;
		try {
		    grade = Double.parseDouble(parts[2].trim());
		} catch (Exception e) {
		    grade = -1;
		}

		// Create new enrollment with info and add to list
		Enrollment enrollment = new Enrollment(student, course);
		enrollment.setGrade(grade);
		enrollmentList.add(enrollment);
		student.addEnrollment(enrollment);
	    }
	} catch (Exception e) {
	    System.err.println("Error reading enrollments from " + filename + ": " + e.getMessage());
	}
	// Sort and return enrollment list
	sortEnrollments(enrollmentList);
	return enrollmentList;
    }

    public static void saveUsers(ArrayList<User> users, String filename) {
	sortUsers(users);
	// Overwrite the users text file with all brand new data
	try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
	    // Example line
	    writer.println("# id;lastname;firstname;email;password;role");

	    // Loop through users and write a formatted line for each user
	    for (User user : users) {
		String line = user.getId() + ";" + user.getLastName() + ";" + user.getFirstName() + ";"
			+ user.getEmail() + ";" + user.getPassword() + ";" + user.getRole();
		writer.println(line);
	    }
	} catch (IOException e) {
	    System.err.println("Error saving users: " + e.getMessage());
	}
    }

    public static void saveCourses(ArrayList<Course> courses, String filename) {
	sortCourses(courses);
	// Overwrite the courses text file with all brand new data
	try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
	    // Example line
	    writer.println("# code;title;credits;type");

	    // Loop through courses and write a formatted line for each course
	    for (Course course : courses) {
		String line = course.getCode() + ";" + course.getTitle() + ";" + course.getCredits() + ";"
			+ course.getType();
		writer.println(line);
	    }
	} catch (IOException e) {
	    System.err.println("Error saving courses: " + e.getMessage());
	}
    }

    public static void saveEnrollments(ArrayList<Enrollment> enrollments, String filename) {
	sortEnrollments(enrollments);
	// Overwrite the enrollments text file with all brand new data
	try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
	    // Example line
	    writer.println("# studentId;courseCode;grade");

	    // Loop through enrollments and write a formatted line for each enrollment
	    for (Enrollment enrollment : enrollments) {
		String line = enrollment.getStudent().getId() + ";" + enrollment.getCourse().getCode() + ";"
			+ enrollment.getGrade();
		writer.println(line);
	    }
	} catch (IOException e) {
	    System.err.println("Error saving enrollments: " + e.getMessage());
	}
    }

    public static void sortUsers(ArrayList<User> users) {
	ArrayList<User> sortedUsers = new ArrayList<>();
	ArrayList<Student> sortedStudents = new ArrayList<>();
	ArrayList<Admin> sortedAdmins = new ArrayList<>();

	// Loop through all users
	while (!users.isEmpty()) {
	    int minIndex = 0;
	    for (int i = 1; i < users.size(); i++) {
		// Get two users to compare
		User userOne = users.get(i);
		User userTwo = users.get(minIndex);

		int compare = userOne.getLastName().compareTo(userTwo.getLastName());

		// Check if users last name are the same or different
		if (compare < 0) {
		    minIndex = i;
		} else if (compare == 0) {
		    // Check if the users first name are the same or different
		    compare = userOne.getFirstName().compareTo(userTwo.getFirstName());
		    if (compare < 0) {
			minIndex = i;
		    } else if (compare == 0) {
			// If names equal, check ID's
			if (userOne.getId().compareTo(userTwo.getId()) < 0) {
			    minIndex = i;
			}
		    }
		}
	    }

	    sortedUsers.add(users.remove(minIndex));
	}

	// Clear users and re-add them sorted alphabetically/numerically
	users.clear();
	users.addAll(sortedUsers);

	// Get Array of Admins, sorted
	for (User user : users) {
	    if (user instanceof Admin) {
		sortedAdmins.add((Admin) user);
	    }
	}

	// Get Array of Students, sorted
	for (User user : users) {
	    if (user instanceof Student) {
		sortedStudents.add((Student) user);
	    }
	}

	// Clear users and add all Admins first, then Students (All Sorted in their
	// respective role)
	users.clear();
	users.addAll(sortedAdmins);
	users.addAll(sortedStudents);
    }

    public static void sortCourses(ArrayList<Course> courses) {
	ArrayList<Course> sortedCourses = new ArrayList<>();

	// Loops through all courses
	while (!courses.isEmpty()) {
	    int minIndex = 0;
	    for (int i = 1; i < courses.size(); i++) {
		// Get two courses and codes to compare
		Course courseOne = courses.get(i);
		Course courseTwo = courses.get(minIndex);

		String courseOneCode = courseOne.getCode();
		String courseTwoCode = courseTwo.getCode();

		// Get the course prefixes and their number
		String courseOnePrefix = "";
		int courseOneNumber = 0;
		// Loop through course code
		for (int j = 0; j < courseOneCode.length(); j++) {
		    if (Character.isLetter(courseOneCode.charAt(j))) {
			courseOnePrefix += courseOneCode.charAt(j);
		    } else {
			courseOneNumber = Integer.parseInt(courseOneCode.substring(j));
			break;
		    }
		}

		String courseTwoPrefix = "";
		int courseTwoNumber = 0;
		for (int j = 0; j < courseTwoCode.length(); j++) {
		    if (Character.isLetter(courseTwoCode.charAt(j))) {
			courseTwoPrefix += courseTwoCode.charAt(j);
		    } else {
			courseTwoNumber = Integer.parseInt(courseTwoCode.substring(j));
			break;
		    }
		}

		// Compare course prefixes
		int compare = courseOnePrefix.compareTo(courseTwoPrefix);
		// Check if courseTwo prefix or courseTwo number takes precedence
		if (compare < 0 || (compare == 0 && courseOneNumber < courseTwoNumber)) {
		    minIndex = i;
		}
	    }

	    sortedCourses.add(courses.remove(minIndex));
	}

	courses.addAll(sortedCourses);
    }

    public static void sortEnrollments(ArrayList<Enrollment> enrollments) {
	ArrayList<Enrollment> sortedEnrollments = new ArrayList<>();

	// Loops through all enrollments
	while (!enrollments.isEmpty()) {
	    int minIndex = 0;
	    for (int i = 1; i < enrollments.size(); i++) {
		// Get two enrollments and their student ID's to compare
		Enrollment enrollmentOne = enrollments.get(i);
		Enrollment enrollmentTwo = enrollments.get(minIndex);

		String enrollmentOneId = enrollmentOne.getStudent().getId();
		String enrollmentTwoId = enrollmentTwo.getStudent().getId();
		// Sort by student ID
		if (enrollmentOneId.compareTo(enrollmentTwoId) < 0) {
		    minIndex = i;
		}
	    }
	    sortedEnrollments.add(enrollments.remove(minIndex));
	}

	enrollments.addAll(sortedEnrollments);
    }

    private static Student findStudentById(ArrayList<User> users, String id) {
	// Loop through all users
	for (User user : users) {
	    // Check if user is a Student and validates the ID
	    if (user instanceof Student && user.getId().equals(id)) {
		return (Student) user;
	    }
	}
	return null;
    }

    private static Course findCourseByCode(ArrayList<Course> courses, String code) {
	// Loops through all courses
	for (Course course : courses) {
	    // Validates the course existence
	    if (course.getCode().equals(code)) {
		return course;
	    }
	}
	return null;
    }
}