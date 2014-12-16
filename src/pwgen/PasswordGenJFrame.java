package pwgen;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.security.SecureRandom;

import javax.swing.*;

import java.util.*;
import java.util.prefs.Preferences;

/**
 * simple secure password generator
 */
public class PasswordGenJFrame extends JFrame {
	
	public static void main (String args[]) {
		PasswordGenJFrame f = new PasswordGenJFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private final SecureRandom secureRandom = new SecureRandom();
	private final JTextArea textArea = new JTextArea();
	private final JSpinner lowerSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner upperSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner anySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner wordSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JButton generateButton = new JButton("Generate");
	private final JButton copyButton = new JButton("Copy");
	private final JCheckBox shuffleBox = new JCheckBox("Shuffle");
	private final Preferences prefs = Preferences.userNodeForPackage(getClass());
	
	public PasswordGenJFrame () {
		super("Password Generator");
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				textArea.setText(generate());
			}
		});
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
				clip.setContents(new StringSelection(textArea.getText()), null);
			}
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent e) {
				savePrefs();
			}
		});
		
		textArea.setColumns(20);
		textArea.setFont(new Font("monospaced", Font.PLAIN, 18));
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		
		JPanel optionPanel = new JPanel();
		optionPanel.add(new JLabel("Upper"));
		optionPanel.add(upperSpinner);
		optionPanel.add(new JLabel("Lower"));
		optionPanel.add(lowerSpinner);
		optionPanel.add(new JLabel("Digit"));
		optionPanel.add(digitSpinner);
		optionPanel.add(new JLabel("Punct"));
		optionPanel.add(punctSpinner);
		optionPanel.add(new JLabel("Any"));
		optionPanel.add(anySpinner);
		
		JPanel optionPanel2 = new JPanel();
		optionPanel2.add(new JLabel("Title Word"));
		optionPanel2.add(wordSpinner);
		optionPanel2.add(shuffleBox);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(textArea);
		buttonPanel.add(generateButton);
		buttonPanel.add(copyButton);
		
		JPanel contentPanel = new JPanel(new GridLayout(3, 1));
		contentPanel.setBorder(BorderFactory.createEmptyBorder());
		contentPanel.add(optionPanel);
		contentPanel.add(optionPanel2);
		contentPanel.add(buttonPanel);
		
		loadPrefs();
		
		setContentPane(contentPanel);
		pack();
	}
	
	private void loadPrefs () {
		System.out.println("load prefs");
		anySpinner.setValue(prefs.getInt("any", 0));
		digitSpinner.setValue(prefs.getInt("digit", 1));
		lowerSpinner.setValue(prefs.getInt("lower", 6));
		punctSpinner.setValue(prefs.getInt("punct", 0));
		shuffleBox.setSelected(prefs.getBoolean("shuffle", false));
		upperSpinner.setValue(prefs.getInt("upper", 1));
		wordSpinner.setValue(prefs.getInt("word", 0));
	}
	
	private void savePrefs () {
		System.out.println("save prefs");
		prefs.putInt("any", (Integer) anySpinner.getValue());
		prefs.putInt("digit", (Integer) digitSpinner.getValue());
		prefs.putInt("lower", (Integer) lowerSpinner.getValue());
		prefs.putInt("punct", (Integer) punctSpinner.getValue());
		prefs.putBoolean("shuffle", shuffleBox.isSelected());
		prefs.putInt("upper", (Integer) upperSpinner.getValue());
		prefs.putInt("word", (Integer) wordSpinner.getValue());
	}
	
	public String generate () {
		List<String> list = new ArrayList<>();
		list.add(upper((Integer) upperSpinner.getValue()));
		list.add(lower((Integer) lowerSpinner.getValue()));
		list.add(digit((Integer) digitSpinner.getValue()));
		list.add(punct((Integer) punctSpinner.getValue()));
		list.add(any((Integer) anySpinner.getValue()));
		list.add(word((Integer) wordSpinner.getValue(), false, true));
		Collections.shuffle(list, secureRandom);
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
		}
		if (shuffleBox.isSelected()) {
			Collections.shuffle(new StringBuilderList(sb), secureRandom);
		}
		return sb.toString();
	}
	
	/**
	 * return a printable ascii character
	 */
	private String any (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			// don't include 0x7f (delete)
			// but do include space
			char c = (char) (secureRandom.nextInt(95) + 32);
			sb.append(c);
		}
		return sb.toString();
	}
	
	private String lower (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('a' + secureRandom.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}
	
	private String upper (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('A' + secureRandom.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}
	
	private String digit (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('0' + secureRandom.nextInt(10));
			sb.append(c);
		}
		return sb.toString();
	}
	
	private String punct (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			while (true) {
				char c = (char) (secureRandom.nextInt(95) + 32);
				if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}
	
	private String hex (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			sb.append("01234567890abcdef".charAt(secureRandom.nextInt(16)));
		}
		return sb.toString();
	}
	
	private String word (int len, boolean upper, boolean title) {
		if (len > 0) {
			StringBuilder sb = new StringBuilder();
			String v = "aeiou";
			for (int n = 0; n < len; n++) {
				while (true) {
					char c = (char) ('a' + secureRandom.nextInt(26));
					if (sb.length() == 0) {
						sb.append(c);
						break;
					} else {
						char p = sb.charAt(sb.length() - 1);
						if ((v.indexOf(p) >= 0 && v.indexOf(c) == -1) || (v.indexOf(p) == -1 && v.indexOf(c) >= 0)) {
							sb.append(c);
							break;
						}
					}
				}
			}
			if (upper) {
				return sb.toString().toUpperCase();
			} else if (title) {
				return Character.toUpperCase(sb.charAt(0)) + sb.substring(1);
			} else {
				return sb.toString();
			}
		}
		return "";
	}
}