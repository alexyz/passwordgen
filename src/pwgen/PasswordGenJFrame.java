package pwgen;

import java.awt.event.*;
import java.util.prefs.Preferences;

import javax.swing.*;

/**
 * simple secure password generator
 */
public class PasswordGenJFrame extends JFrame {

	private final JTabbedPane tabs = new JTabbedPane();
	private final CharPasswordJPanel charPassPanel = new CharPasswordJPanel();
	private final WordPasswordJPanel wordPassPanel = new WordPasswordJPanel();
	private final DictPasswordJPanel dictPassPanel = new DictPasswordJPanel();
	private final BitsPasswordJPanel bitsPassPanel = new BitsPasswordJPanel();
	private final PinPasswordJPanel pinPassPanel = new PinPasswordJPanel();
	private final UuidPasswordJPanel uuidPassPanel = new UuidPasswordJPanel();
	
	public static void main (String args[]) {
		PasswordGenJFrame f = new PasswordGenJFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	public PasswordGenJFrame () {
		super("Password Generator [https://github.com/alexyz/passwordgen]");
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent ae) {
				savePrefs();
			}
		});
		
		tabs.add("Character", charPassPanel);
		tabs.add("Word", wordPassPanel);
		tabs.add("Dictionary", dictPassPanel);
		tabs.add("Bits", bitsPassPanel);
		tabs.add("PIN", pinPassPanel);
		tabs.add("UUID", uuidPassPanel);
		
		loadPrefs();
		
		setContentPane(tabs);
		pack();
	}

	private void loadPrefs () {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		tabs.setSelectedIndex(prefs.getInt("index", 0));
		try {
			charPassPanel.loadPrefs();
			wordPassPanel.loadPrefs();
			dictPassPanel.loadPrefs();
			bitsPassPanel.loadPrefs();
			pinPassPanel.loadPrefs();
			uuidPassPanel.loadPrefs();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.toString());
		}
	}

	private void savePrefs () {
		try {
			System.out.println("save prefs");
			Preferences prefs = Preferences.userNodeForPackage(getClass());
			prefs.putInt("index", tabs.getSelectedIndex());
			charPassPanel.savePrefs();
			wordPassPanel.savePrefs();
			dictPassPanel.savePrefs();
			bitsPassPanel.savePrefs();
			pinPassPanel.savePrefs();
			uuidPassPanel.savePrefs();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.toString());
		}
	}
}