package pwgen;

import java.util.Collections;
import java.util.prefs.Preferences;

import javax.swing.*;

public class CharPasswordJPanel extends PasswordJPanel {
	
	private final JSpinner lowerSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner upperSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner anySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JCheckBox shuffleBox = new JCheckBox("Shuffle");
	
	public CharPasswordJPanel () {
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
		optionPanel.add(shuffleBox);
	}
	
	@Override
	protected void loadPrefs () throws Exception {
		System.out.println("load prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		lowerSpinner.setValue(prefs.getInt("lower", 6));
		upperSpinner.setValue(prefs.getInt("upper", 1));
		digitSpinner.setValue(prefs.getInt("digitx", 1));
		punctSpinner.setValue(prefs.getInt("punct", 0));
		anySpinner.setValue(prefs.getInt("any", 0));
		shuffleBox.setSelected(prefs.getBoolean("shufflex", false));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		System.out.println("save prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("upper", (Integer) upperSpinner.getValue());
		prefs.putInt("lower", (Integer) lowerSpinner.getValue());
		prefs.putInt("digitx", (Integer) digitSpinner.getValue());
		prefs.putInt("punct", (Integer) punctSpinner.getValue());
		prefs.putInt("any", (Integer) anySpinner.getValue());
		prefs.putBoolean("shufflex", shuffleBox.isSelected());
		prefs.flush();
	}
	
	@Override
	public String generate () {
		StringBuilder sb = new StringBuilder();
		sb.append(upper((Integer) upperSpinner.getValue()));
		sb.append(lower((Integer) lowerSpinner.getValue()));
		sb.append(digit((Integer) digitSpinner.getValue()));
		sb.append(punct((Integer) punctSpinner.getValue()));
		sb.append(any((Integer) anySpinner.getValue()));
		if (shuffleBox.isSelected()) {
			Collections.shuffle(new StringBuilderList(sb), RANDOM);
		}
		return sb.toString();
	}
}
