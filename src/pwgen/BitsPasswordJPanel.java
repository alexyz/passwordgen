package pwgen;

import java.util.Collections;
import java.util.prefs.Preferences;

import javax.swing.*;

public class BitsPasswordJPanel extends PasswordJPanel {
	
	private final JSpinner bitsSpinner = new JSpinner(new SpinnerNumberModel(8, 8, 4096, 8));
	private final JCheckBox b64CheckBox = new JCheckBox("Base64");
	
	public BitsPasswordJPanel () {
		optionPanel.add(new JLabel("Bits"));
		optionPanel.add(bitsSpinner);
		optionPanel.add(b64CheckBox);
	}
	
	@Override
	protected void loadPrefs () throws Exception {
		System.out.println("load prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		bitsSpinner.setValue(Integer.valueOf(prefs.getInt("bits", 64)));
		b64CheckBox.setSelected(prefs.getBoolean("b64", true));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		System.out.println("save prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("bits", ((Number) bitsSpinner.getValue()).intValue());
		prefs.putBoolean("any", b64CheckBox.isSelected());
		prefs.flush();
	}
	
	@Override
	public String generate () {
		StringBuilder sb = new StringBuilder();
		int bits = ((Number) bitsSpinner.getValue()).intValue();
		sb.append(bits(bits, b64CheckBox.isSelected()));
		Collections.shuffle(new StringBuilderList(sb), RANDOM);
		return sb.toString();
	}
}
