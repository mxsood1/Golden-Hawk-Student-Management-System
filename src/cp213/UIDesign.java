package cp213;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class UIDesign {

    private static final Color LAURIER_PURPLE = new Color(75, 0, 130);
    private static final Color LAURIER_GOLD = new Color(255, 204, 0);
    private static final Color WHITE = Color.white;

    public static void apply() {
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (Exception ignored) {
	}

	// Backgrounds
	UIManager.put("Panel.background", LAURIER_PURPLE);
	UIManager.put("OptionPane.background", LAURIER_PURPLE);
	UIManager.put("ScrollPane.background", LAURIER_PURPLE);
	UIManager.put("Viewport.background", LAURIER_PURPLE);

	// Pop-up text color
	UIManager.put("OptionPane.messageForeground", LAURIER_GOLD);

	// Labels
	UIManager.put("Label.foreground", LAURIER_GOLD);

	// Buttons
	UIManager.put("Button.background", LAURIER_GOLD);
	UIManager.put("Button.foreground", LAURIER_PURPLE);
	Border buttonBorder = BorderFactory.createLineBorder(LAURIER_PURPLE, 2);
	UIManager.put("Button.border", buttonBorder);
	UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // hide ugly focus

	// Text fields
	UIManager.put("TextField.background", WHITE);
	UIManager.put("TextField.foreground", LAURIER_PURPLE);
	UIManager.put("TextField.caretForeground", LAURIER_PURPLE);
	UIManager.put("TextField.border", BorderFactory.createLineBorder(LAURIER_GOLD, 2));

	// Lists
	UIManager.put("List.background", LAURIER_PURPLE);
	UIManager.put("List.foreground", LAURIER_GOLD);
	UIManager.put("List.selectionBackground", LAURIER_GOLD);
	UIManager.put("List.selectionForeground", LAURIER_PURPLE);

	// Combo boxes
	UIManager.put("ComboBox.background", WHITE);
	UIManager.put("ComboBox.foreground", LAURIER_PURPLE);
	UIManager.put("ComboBox.selectionBackground", LAURIER_PURPLE);
	UIManager.put("ComboBox.selectionForeground", WHITE);

	// Titled borders (like "Add Student", "Students", etc.)
	UIManager.put("TitledBorder.titleColor", WHITE);
    }
}