package pwgen;

import java.util.Collections;
import java.util.prefs.Preferences;

import javax.swing.*;

import static pwgen.PwUtil.*;

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
		lowerSpinner.setValue(prefs.getInt("chlower", 5));
		upperSpinner.setValue(prefs.getInt("chupper", 1));
		digitSpinner.setValue(prefs.getInt("chdigit", 1));
		punctSpinner.setValue(prefs.getInt("chpunct", 1));
		anySpinner.setValue(prefs.getInt("chany", 0));
		shuffleBox.setSelected(prefs.getBoolean("chshuf", true));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		System.out.println("save prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("chupper", intValue(upperSpinner));
		prefs.putInt("chlower", intValue(lowerSpinner));
		prefs.putInt("chdigit", intValue(digitSpinner));
		prefs.putInt("chpunct", intValue(punctSpinner));
		prefs.putInt("chany", intValue(anySpinner));
		prefs.putBoolean("chshuf", shuffleBox.isSelected());
		prefs.flush();
	}
	
	@Override
	public void generate () {
		StringBuilder sb = new StringBuilder();
		int u = intValue(upperSpinner);
		int l = intValue(lowerSpinner);
		int d = intValue(digitSpinner);
		int p = intValue(punctSpinner);
		int a = intValue(anySpinner);
		sb.append(upper(u));
		sb.append(lower(l));
		sb.append(digit(d));
		sb.append(punct(p));
		sb.append(any(a));
		double v = pow(26, u + l) + pow(10, d) + pow(PUNCT, p) + pow(ANY, a);
		System.out.println("v=" + v);
		if (shuffleBox.isSelected()) {
			Collections.shuffle(new StringBuilderList(sb), RANDOM);
			double f = fac(sb.length()) / (fac(u)*fac(l)*fac(d)*fac(p)*fac(a));
			v = v * f;
		}
		setValue(sb.toString(), v);
	}
}
