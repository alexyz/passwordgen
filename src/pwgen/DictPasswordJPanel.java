package pwgen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import javax.swing.*;

public class DictPasswordJPanel extends PasswordJPanel {
	
	private static final int MAX = 8;
	private static final int MIN = 4;
	
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
		Pattern p = Pattern.compile("[a-z]+");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			String l;
			while ((l = br.readLine()) != null) {
				// try to avoid poor quality words
				l = l.trim().toLowerCase();
				if (p.matcher(l).matches() && l.length() >= MIN && l.length() <= MAX) {
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
		
		if (dict.size() == 0) {
			JOptionPane.showMessageDialog(this, "No words in dictionary");
			return "";
		}
		
		List<String> parts = new ArrayList<>();
		
		{
			int len = ((Integer)wordSpinner.getValue()).intValue();
			
			List<Integer> seq = seq(len);
			if (seq == null) {
				JOptionPane.showMessageDialog(this, "Could not sequence");
				return "";
			}
			
			for (int x : seq) {
				Set<String> s = dict.get(x);
				if (s != null && s.size() > 0) {
					String[] a = s.toArray(new String[s.size()]);
					String word = a[RANDOM.nextInt(a.length)];
					if (titleBox.isSelected()) {
						word = word.substring(0, 1).toUpperCase() + word.substring(1);
					}
					parts.add(word);
				}
			}
		}
		
		{
			int dig = ((Integer)digitSpinner.getValue()).intValue();
			if (dig > 0) {
				parts.add(digit(dig));
			}
		}
		
		{
			int pun = ((Integer)punctSpinner.getValue()).intValue();
			if (pun > 0) {
				parts.add(punct(pun));
			}
		}
		
		Collections.shuffle(parts, RANDOM);
		return String.join("", parts);
	}
	
	private List<Integer> seq (int len) {
		
		List<List<Integer>> seqs = new ArrayList<>();
		
		if (dict.keySet().contains(len)) {
			seqs.add(Arrays.asList(len));
		}
		
		if (seqs.size() == 0 || len >= MAX) {
			List<Integer> seq = sumseq(len);
			if (seq != null) {
				seqs.add(seq);
			}
		}
		
		return seqs.size() > 0 ? seqs.get(RANDOM.nextInt(seqs.size())) : null;
	}
	
	private List<Integer> sumseq (int len) {
		// add sizes to list
		// shuffle list
		// pick a subset...
		Integer[] keys = dict.keySet().toArray(new Integer[dict.size()]);
		List<Integer> seq = new ArrayList<>();
		for (int n = 0; n < keys.length; n++) {
			if (keys[n] <= len - MIN) {
				for (int m = 0; m < len - MIN; m++) {
					seq.add(keys[n]);
				}
			}
		}
//		System.out.println("seq=" + seq);
		for (int n = 0; n < 100; n++) {
			Collections.shuffle(seq, RANDOM);
			int sum = 0;
			for (int m = 0; m < seq.size() && sum < len; m++) {
				sum += seq.get(m);
				if (sum == len) {
//					System.out.println("n=" + n);
					return new ArrayList<>(seq.subList(0, m + 1));
				}
			}
		}
		return null;
	}
}
