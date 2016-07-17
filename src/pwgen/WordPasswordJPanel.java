package pwgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.*;

public class WordPasswordJPanel extends PasswordJPanel {
	
	private final JSpinner wordSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JCheckBox titleBox = new JCheckBox("Title");
	
	public WordPasswordJPanel () {
		optionPanel.add(new JLabel("Word"));
		optionPanel.add(wordSpinner);
		optionPanel.add(new JLabel("Digit"));
		optionPanel.add(digitSpinner);
		optionPanel.add(titleBox);
	}
	
	@Override
	protected void loadPrefs () {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		wordSpinner.setValue(prefs.getInt("words", 7));
		digitSpinner.setValue(prefs.getInt("digit", 1));
		titleBox.setSelected(prefs.getBoolean("title", false));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("words", (Integer) wordSpinner.getValue());
		prefs.putInt("digit", (Integer) digitSpinner.getValue());
		prefs.putBoolean("title", titleBox.isSelected());
		prefs.sync();
	}
	
	@Override
	public String generate () {
		List<String> list = new ArrayList<>();
		int word = ((Integer) wordSpinner.getValue()).intValue();
		int digit = ((Integer) digitSpinner.getValue()).intValue();
		
		list.add(word(word, titleBox.isSelected()));
		list.add(digit(digit));
		Collections.shuffle(list, RANDOM);
		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	protected String word (int len, boolean title) {
		if (len > 0) {
			StringBuilder sb = new StringBuilder();
			String v = "aeiou";
			for (int n = 0; n < len; n++) {
				while (true) {
					char c = (char) ('a' + RANDOM.nextInt(26));
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
			if (title) {
				sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			}
			return sb.toString();
		}
		return "";
	}
}
