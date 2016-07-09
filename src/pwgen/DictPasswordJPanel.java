package pwgen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.Preferences;

import javax.swing.*;

public class DictPasswordJPanel extends PasswordJPanel {
	
	private final JLabel fileLabel = new JLabel();
	private final JButton fileButton = new JButton("File...");
	private final JSpinner wordSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final SortedMap<Integer,Set<String>> dict = new TreeMap<>();
	private final JCheckBox titleBox = new JCheckBox("Title");
	
	private File file;
	
	public DictPasswordJPanel () {
		
		fileButton.addActionListener(e -> file());
		
		optionPanel.add(fileLabel);
		optionPanel.add(fileButton);
		optionPanel.add(new JLabel("Word"));
		optionPanel.add(wordSpinner);
		optionPanel.add(new JLabel("Digit"));
		optionPanel.add(digitSpinner);
		optionPanel.add(new JLabel("Punct"));
		optionPanel.add(punctSpinner);
		optionPanel.add(titleBox);
	}
	
	private void file () {
		JFileChooser fc = new JFileChooser();
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			if (fc.getSelectedFile().isFile()) {
				file = fc.getSelectedFile();
				fileLabel.setText(file.getName());
				loadfile();
			}
		}
	}

	private void loadfile () {
		dict.clear();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			String l;
			while ((l = br.readLine()) != null) {
				l = l.trim().toLowerCase();
				if (!l.endsWith("'s")) {
					dict.compute(l.length(), (k,v) -> v != null ? v : new TreeSet<>()).add(l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.toString());
		}
	}

	@Override
	protected void loadPrefs () {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		wordSpinner.setValue(prefs.getInt("len", 7));
		digitSpinner.setValue(prefs.getInt("digit", 1));
		punctSpinner.setValue(prefs.getInt("punct", 1));
		titleBox.setSelected(prefs.getBoolean("title", false));
		String f = prefs.get("file", "");
		System.out.println("loaded file pref " + f);
		if (f.length() > 0) {
			file = new File(f);
			fileLabel.setText(file.getName());
		}
	}
	
	@Override
	protected void savePrefs () throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("len", (Integer) wordSpinner.getValue());
		prefs.putInt("digit", (Integer) digitSpinner.getValue());
		prefs.putInt("punct", (Integer) punctSpinner.getValue());
		prefs.putBoolean("title", titleBox.isSelected());
		if (file != null) {
			System.out.println("save file pref " + file.getAbsolutePath());
			prefs.put("file", file.getAbsolutePath());
		}
		prefs.sync();
	}
	
	@Override
	public String generate () {
		if (dict.size() == 0) {
			loadfile();
		}
		int wordlen = ((Integer)wordSpinner.getValue()).intValue();
		int dig = ((Integer)digitSpinner.getValue()).intValue();
		int pun = ((Integer)punctSpinner.getValue()).intValue();
		
		List<String> l = new ArrayList<>();
		if (pun > 0) {
			l.add(punct(pun));
		}
		if (dig > 0) {
			l.add(digit(dig));
		}
		Set<String> s = dict.get(wordlen);
		List<String> dictlist = new ArrayList<>(s);
		String word = dictlist.get(RANDOM.nextInt(dictlist.size()));
		if (titleBox.isSelected()) {
			word = word.substring(0, 1).toUpperCase() + word.substring(1);
		}
		l.add(word);
		Collections.shuffle(l, RANDOM);
		
		return String.join("", l);
	}
	
}
