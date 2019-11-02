package pwgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.*;

import static pwgen.PwUtil.*;

public class WordPasswordJPanel extends PasswordJPanel {
	
	private final JSpinner wordSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JCheckBox titleBox = new JCheckBox("Title");
	private final JCheckBox shuffleBox = new JCheckBox("Shuffle");
	
	public WordPasswordJPanel () {
		optionPanel.add(new JLabel("Word"));
		optionPanel.add(wordSpinner);
		optionPanel.add(new JLabel("Digit"));
		optionPanel.add(digitSpinner);
		optionPanel.add(new JLabel("Punct"));
		optionPanel.add(punctSpinner);
		optionPanel.add(titleBox);
		optionPanel.add(shuffleBox);
	}
	
	@Override
	protected void loadPrefs () {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		wordSpinner.setValue(prefs.getInt("wdwords", 7));
		digitSpinner.setValue(prefs.getInt("wddigit", 1));
		titleBox.setSelected(prefs.getBoolean("wdtitle", true));
		shuffleBox.setSelected(prefs.getBoolean("wdshuf", false));
		punctSpinner.setValue(prefs.getInt("wdpunct", 1));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("wdwords", intValue(wordSpinner));
		prefs.putInt("wddigit", intValue(digitSpinner));
		prefs.putBoolean("wdtitle", titleBox.isSelected());
		prefs.putBoolean("wdshuf", shuffleBox.isSelected());
		prefs.putInt("wdpunct", intValue(punctSpinner));
		prefs.sync();
	}
	
	@Override
	public void generate () {
		List<String> list = new ArrayList<>();
		int w = intValue(wordSpinner);
		int d = intValue(digitSpinner);
		int p = intValue(punctSpinner);
		
		list.add(word(w, titleBox.isSelected()));
		list.add(digit(d));
		list.add(punct(p));

		if (shuffleBox.isSelected()) {
			Collections.shuffle(list, RANDOM);
		}

		StringBuilder sb = new StringBuilder();
		for (String s : list) {
			sb.append(s);
		}
		setValue(sb.toString(), 0);
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
