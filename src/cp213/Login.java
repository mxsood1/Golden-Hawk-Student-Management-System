package cp213;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JDialog {
    // Text field where user enters their email and password
    private JTextField emailField;
    private JTextField passwordField;
    private boolean succeeded = false;
    // List of all known users (students + admins)
    private ArrayList<User> users;
    private User loggedInUser;

    public Login(Frame parent, ArrayList<User> users) {
	super(parent, "Login", true);
	this.users = users;
	// Main panel with a grid layout
	JPanel panel = new JPanel(new GridLayout(4, 2, 5, 10));
	panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	// Input fields for email and password
	emailField = new JTextField(21);
	passwordField = new JTextField(15);
	// Buttons for confirming or cancelling login
	JButton okButton = new JButton("Login");
	JButton cancelButton = new JButton("Cancel");
	// Four rows and their respective elements
	panel.add(new JLabel("Email:"));
	panel.add(emailField);
	panel.add(new JLabel("Password:"));
	panel.add(passwordField);
	panel.add(new JLabel(""));
	panel.add(new JLabel(""));
	panel.add(okButton);
	panel.add(cancelButton);
	// Add panel to dialog and adjust
	getContentPane().add(panel);
	pack();
	setResizable(false);
	setLocationRelativeTo(parent);
	// Authenticate login
	okButton.addActionListener(e -> doLogin());
	cancelButton.addActionListener(e -> {
	    succeeded = false;
	    dispose();
	});
    }

    private void doLogin() {
	// Get text from input fields
	String email = emailField.getText().trim();
	String password = passwordField.getText().trim();
	// Scan through all users
	for (User user : users) {
	    // Check if email and password match this user
	    if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
		loggedInUser = user;
		succeeded = true;
		dispose();
		return;
	    }
	}

	JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
	succeeded = false;
    }

    public boolean isSucceeded() {
	return succeeded;
    }

    public User getLoggedInUser() {
	return loggedInUser;
    }
}