# Golden Hawk Student Management System

The **Golden Hawk Student Management System** is a fully‑engineered student administration platform built in Java.  Designed as a hackathon project and awarded **Best Project** at Wilfrid Laurier University, the system provides a clean separation between the user interface, business logic and persistent storage.  It offers a polished user experience, role‑based access and a lightweight, file‑based data store, making it easy to run and extend.

## Features

### 🏆 Award‑winning architecture

The application is more than a simple GUI – it is a complete system with multiple layers and thoughtful design:

- **Role‑based login.** Users authenticate via a modal login dialog that compares the entered email and password against a list of known users.  On success the user is returned and the dialog closes; on failure an error message is shown.
- **Multi‑layer architecture.** The system separates the interface, logic and data persistence.  The `GoldenHawkApp` class loads users, courses and enrollments from text files and then builds a tabbed window of panels for Students, Courses and Enrollments.  Business logic such as adding, searching or deleting records lives in dedicated methods, while data storage responsibilities are handled by `DataStorage`.
- **Student management.** Administrators can add new students by filling out a form with ID, first and last name, email and password.  A list below displays all students and a search/delete panel allows records to be filtered or removed.
- **Course management.** Courses include a code, title, credit weight and type (Lecture, Lab or Tutorial).  Administrators can add courses, search for courses or delete selected courses; students see only the courses in which they are enrolled.
- **Enrollment & grading.** Both roles can enroll students in courses via drop‑down lists.  Administrators have an additional row to set numeric grades and calculate GPA.  Students may view their own grades and request a GPA calculation.
- **File‑based persistence.** Data is stored in simple text files.  `DataStorage.loadUsers` reads each line of the users file, splits it on semicolons and instantiates either an `Admin` or a `Student` based on the role.  `loadCourses` parses course records, converts the credit value to a number and selects the appropriate course subclass (Lecture, Lab or Tutorial).  `loadEnrollments` matches a student ID and course code from each line, parses the numeric grade and builds an `Enrollment` object.  When data is modified, corresponding save methods overwrite these files with sorted records to keep ordering consistent.
- **Polished UI.** A dedicated `UIDesign` class applies a custom colour scheme inspired by Laurier’s purple and gold.  It sets background and foreground colours for panels, lists, buttons, text fields and combo boxes, ensuring a cohesive look across the application.  The tabbed interface offers intuitive navigation between Students, Courses and Enrollments.
- **Real‑time validation and error handling.** Actions such as adding a student or course perform checks for missing fields, duplicate IDs or codes, numeric credit values and grade ranges.  Invalid inputs trigger descriptive dialog messages to keep the application stable.

## Project structure

```
├── src/cp213/
│   ├── GoldenHawkApp.java  # Main window that builds tabs and handles actions
│   ├── Login.java          # Modal login dialog for authentication
│   ├── DataStorage.java    # Reads/writes users, courses & enrollments from text files
│   ├── UIDesign.java       # Applies custom colour scheme across Swing UI
│   ├── User.java, Student.java, Admin.java   # Domain classes representing users
│   ├── Course.java, Lecture.java, Lab.java, Tutorial.java  # Course hierarchy
│   ├── Enrollment.java     # Associates a Student with a Course and optional grade
│   └── …                   # Additional helper classes and utilities
├── users.txt        # Sample users with id;lastname;firstname;email;password;role
├── courses.txt      # Sample courses with code;title;credits;type
├── enrollments.txt  # Sample enrollments with studentId;courseCode;grade
└── README.md        # Project documentation (this file)
```

## Getting started

### Prerequisites

- **Java Development Kit (JDK) 8+** – the project uses plain Java and Swing; no external libraries are required.
- A terminal or IDE such as IntelliJ or Eclipse.

### Setup

1. **Clone the repository.**

   ```sh
   git clone https://github.com/mxsood1/Golden-Hawk-Student-Management-System.git
   cd Golden-Hawk-Student-Management-System
   ```

2. **Ensure data files exist.**  The application reads `users.txt`, `courses.txt` and `enrollments.txt` on startup.  Each file uses semicolon‑separated values.  For example, user records follow `id;lastname;firstname;email;password;role` and course records follow `code;title;credits;type`.

3. **Compile the source.**  You can compile all classes into a `bin` directory:

   ```sh
   mkdir -p bin
   javac -d bin src/cp213/*.java
   ```

4. **Run the application.**  Launch the system by running the `GoldenHawkApp` class.  You may start with the login dialog by instantiating `Login` and then constructing `GoldenHawkApp` based on the returned user.  For a quick test, you can run:

   ```sh
   java -cp bin cp213.GoldenHawkApp
   ```

   When the window appears, enter an email and password from `users.txt` (e.g., `admin001@mylaurier.ca` / `admin123` for an admin or a student credential for a student).  Administrators will see three tabs (Students, Courses and Enrollments & Grades); students will see only the Enrollments tab and their own courses.

## Usage

- **Manage students (Admin).**  On the *Students* tab, fill in a new student’s ID, first name, last name, email and password to add them to the system.  Use the search field to filter by ID or name, and click *Delete Selected Student* to remove a selected record.
- **Manage courses (Admin).**  On the *Courses* tab, supply a course code, title, credit weight and choose a type (Lecture, Lab or Tutorial) to add a new course.  Search by code or title and delete selected courses when necessary.
- **Enroll & grade.**  On the *Enrollments & Grades* tab, select a student and a course from the drop‑down lists and click *Enroll*.  Administrators may enter a numeric grade (0.0 – 100.0) and assign it using *Set Grade*.  Both roles can calculate the student’s GPA.
- **Real‑time feedback.**  The system checks for duplicate IDs/codes, non‑numeric credit values, missing fields and invalid grade ranges.  Errors trigger pop‑up messages and prevent corrupt data from being saved.

## Data file formats

The built‑in text files use a simple structure to make testing and extension easy.  The application will create these files on save if they do not exist.

- **users.txt** – each non‑comment line has six values: `id;lastname;firstname;email;password;role`.  Roles must be `ADMIN` or `STUDENT`.
- **courses.txt** – each entry contains `code;title;credits;type`.  The type must be `Lecture`, `Lab` or `Tutorial`.
- **enrollments.txt** – each line lists `studentId;courseCode;grade`.  Grades may be empty or numeric; an empty grade is interpreted as “not yet graded”.

You can edit these files directly with a text editor or use the application’s interface to manage records.  When saving, `DataStorage` writes a header line beginning with `#` for clarity and sorts the records for readability.

## Extending the system

This repository serves as a foundation for more advanced student information systems.  Potential enhancements include:

1. **Database storage.** Replace the text file persistence with a relational database (e.g., SQLite or PostgreSQL) and refactor `DataStorage` accordingly.
2. **User authentication improvements.** Store salted password hashes instead of plain text and implement password reset functionality.
3. **More roles and privileges.** Add instructor or teaching assistant roles with permissions tailored to their tasks.
4. **Web or mobile interface.** Port the logic to a web framework or create a REST API to allow browser‑based or mobile clients.

## Awards & recognition

The Golden Hawk Student Management System earned the **Best Project** award at the Wilfrid Laurier University hackathon.  Selected from among sixteen teams and more than fifty participants, it was recognized for its robust architecture, polished interface and attention to detail.  This distinction underscores the quality of the design and the collaborative effort behind it, demonstrating that a thoughtfully engineered solution can stand out in a crowded field.
 more than fifty participants, it was recognized for its robust architecture, polished interface and attention to detail.  This distinction underscores the quality of the design and the collaborative effort behind it, demonstrating that a thoughtfully engineered solution can stand out in a crowded field.
