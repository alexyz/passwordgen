package pwgen;

import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.prefs.Preferences;

import javax.swing.*;

/**
 * simple secure password generator
 */
public class PasswordGenJFrame extends JFrame {
	
	private final JTabbedPane tabs = new JTabbedPane();
	private final CharPasswordJPanel charPassPanel = new CharPasswordJPanel();
	private final WordPasswordJPanel wordPassPanel = new WordPasswordJPanel();
	
	public static void main (String args[]) {
		PasswordGenJFrame f = new PasswordGenJFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private static String getDateStamp() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		if (cl instanceof URLClassLoader) {
			URLClassLoader ucl = (URLClassLoader) cl;
			URL url = ucl.findResource("META-INF/MANIFEST.MF");
			if (url != null) {
				try {
					Manifest manifest = new Manifest(url.openStream());
					Attributes attributes = manifest.getMainAttributes();
					String dateStamp = attributes.getValue("DSTAMP");
					if (dateStamp != null) {
						return dateStamp;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	
	public PasswordGenJFrame () {
		super("Password Generator " + getDateStamp());
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent ae) {
				savePrefs();
			}

		});
		
		tabs.add("Character", charPassPanel);
		tabs.add("Word", wordPassPanel);
		
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
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.toString());
		}
	}
}