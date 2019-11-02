package pwgen;

import java.util.prefs.Preferences;

import javax.swing.*;

import static pwgen.PwUtil.*;

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
		bitsSpinner.setValue(prefs.getInt("btbits", 64));
		b64CheckBox.setSelected(prefs.getBoolean("btb64", true));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		System.out.println("save prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("btbits", intValue(bitsSpinner));
		prefs.putBoolean("btb64", b64CheckBox.isSelected());
		prefs.flush();
	}
	
	@Override
	public void generate () {
		StringBuilder sb = new StringBuilder();
		int b = intValue(bitsSpinner);
		sb.append(bits(b, b64CheckBox.isSelected()));
		setValue(sb.toString(), Math.pow(2, b));
	}
}
