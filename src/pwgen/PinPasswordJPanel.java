package pwgen;

import javax.swing.*;
import java.util.Collections;
import java.util.prefs.Preferences;

import static pwgen.PwUtil.*;

public class PinPasswordJPanel extends PasswordJPanel {

	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JCheckBox shuffleBox = new JCheckBox("Shuffle");
	private final JCheckBox uniqueBox = new JCheckBox("Unique");

	public PinPasswordJPanel() {
		optionPanel.add(new JLabel("Digit"));
		optionPanel.add(digitSpinner);
		optionPanel.add(shuffleBox);
		optionPanel.add(uniqueBox);
	}
	
	@Override
	protected void loadPrefs () throws Exception {
		System.out.println("load prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		digitSpinner.setValue(prefs.getInt("pindigit", 6));
		shuffleBox.setSelected(prefs.getBoolean("pinshuf", true));
		uniqueBox.setSelected(prefs.getBoolean("pinuniq", true));
	}
	
	@Override
	protected void savePrefs () throws Exception {
		System.out.println("save prefs");
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("pindigit", intValue(digitSpinner));
		prefs.putBoolean("pinshuf", shuffleBox.isSelected());
		prefs.putBoolean("pinuniq", uniqueBox.isSelected());
		prefs.flush();
	}
	
	@Override
	public void generate () {
		int d = intValue(digitSpinner);
		StringBuilderList sbl = new StringBuilderList(new StringBuilder());

		if (uniqueBox.isSelected()) {
			if (d > 10) {
				JOptionPane.showMessageDialog(this, "too many digits for unique");
				return;
			}
			// add all digits then remove
			for (int n = 0; n < 10; n++) {
				sbl.sb.append(n);
			}
			Collections.shuffle(sbl);
			while (sbl.size() > d) {
				sbl.remove(0);
			}
		} else {
			for (int n = 0; n < d; n++) {
				sbl.sb.append(RANDOM.nextInt(10));
			}
		}

		if (shuffleBox.isSelected()) {
			Collections.shuffle(sbl);
		} else {
			Collections.sort(sbl);
		}

		setValue(sbl.sb.toString(), 0, false);
	}
}
