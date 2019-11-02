package pwgen;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import javax.swing.*;

import static pwgen.PwUtil.*;

public class DictPasswordJPanel extends PasswordJPanel {

    private static final int MIN = 3;
    private static final int MAX = 13;
	private static Comparator<List<Integer>> LI_CMP = new Comparator<List<Integer>>() {
		@Override
		public int compare(List<Integer> l1, List<Integer> l2) {
			int c = l1.size() - l2.size();
			for (int n = 0; c == 0 && n < l1.size(); n++) {
				c = l1.get(n) - l2.get(n);
			}
			return c;
		}
	};
	
	private final JLabel fileLabel = new JLabel();
	private final JButton fileButton = new JButton("File...");
	private final JSpinner wordSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	/** map of word length to list of words */
	private final SortedMap<Integer,List<String>> dict = new TreeMap<>();
	private final JCheckBox titleBox = new JCheckBox("Title");
	private final JCheckBox shuffleBox = new JCheckBox("Shuffle");
	
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
		optionPanel.add(shuffleBox);
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
		Map<Integer,Set<String>> dictset = new TreeMap<>();
		Pattern p = Pattern.compile("[a-z]+");
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			String l;
			while ((l = br.readLine()) != null) {
				// try to avoid poor quality words
				l = l.trim().toLowerCase();
				if (p.matcher(l).matches() && l.length() >= MIN && l.length() <= MAX) {
					dictset.computeIfAbsent(Integer.valueOf(l.length()), k -> new TreeSet<>()).add(l);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, e.toString());
		}
		dict.clear();
		for (Map.Entry<Integer,Set<String>> e : dictset.entrySet()) {
			dict.put(e.getKey(), new ArrayList<>(e.getValue()));
		}
	}
	
	@Override
	protected void loadPrefs () {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		wordSpinner.setValue(prefs.getInt("dclen", 6));
		digitSpinner.setValue(prefs.getInt("dcdigit", 1));
		punctSpinner.setValue(prefs.getInt("dcpunct", 1));
		titleBox.setSelected(prefs.getBoolean("dctitle", true));
		shuffleBox.setSelected(prefs.getBoolean("dcshuf", true));
		String f = prefs.get("dcfile", "");
		System.out.println("loaded file pref " + f);
		if (f.length() > 0) {
			file = new File(f);
			fileLabel.setText(file.getName());
		}
	}
	
	@Override
	protected void savePrefs () throws Exception {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		prefs.putInt("dclen", intValue(wordSpinner));
		prefs.putInt("dcdigit", intValue(digitSpinner));
		prefs.putInt("dcpunct", intValue(punctSpinner));
		prefs.putBoolean("dctitle", titleBox.isSelected());
		prefs.putBoolean("dcshuf", shuffleBox.isSelected());
		if (file != null) {
			System.out.println("save file pref " + file.getAbsolutePath());
			prefs.put("dcfile", file.getAbsolutePath());
		}
		prefs.sync();
	}

	private Set<List<Integer>> partitions(int n, int s) {
		Set<List<Integer>> out = new TreeSet<>(LI_CMP);
		if (n == 1 && s >= MIN && s <= MAX) {
			out.add(Arrays.asList(Integer.valueOf(s)));
		} else if (n > 1 && s >= MIN) {
			for (int i = MIN; i < MAX; i++) {
				for (List<Integer> j : partitions(n - 1, s - i)) {
					out.add(join(j, i));
				}
			}
		}
		return out.size() > 0 ? out : Collections.emptySet();
	}

	private List<Integer> join (List<Integer> l, int i) {
		List<Integer> l2 = new ArrayList<>();
		l2.addAll(l);
		l2.add(Integer.valueOf(i));
		Collections.sort(l2);
		return l2;
	}

	private long dictproduct(List<Integer> x) {
		long v = 1;
		for (Integer i : x) {
			List<String> s = dict.get(i);
			v = v * (s != null ? s.size() : 0);
		}
		return v;
	}

	private long sum (List<Long> l) {
		long v = 0;
		for (Long x : l) {
			v = v + x;
		}
		return v;
	}
	
	@Override
	public void generate () {
		if (dict.size() == 0) {
			loadfile();
		}

		if (dict.size() == 0) {
			JOptionPane.showMessageDialog(this, "No words in dictionary");
			return;
		}

		List<String> list = new ArrayList<>();

		int wordlen = intValue(wordSpinner);
		List<List<Integer>> plist = new ArrayList<>();
		List<Long> pcounts = new ArrayList<>();
		for (int n = 1; n < (wordlen / MIN); n++) {
			Set<List<Integer>> pset = partitions(n, wordlen);
			//System.out.println("parts(" + n + ")=" + pset);
			for (List<Integer> p : pset) {
				plist.add(p);
				pcounts.add(Long.valueOf(dictproduct(p)));
				//System.out.println("part=" + p + " prod(p)=" + dictproduct(p));
			}
		}

		long ptotal = sum(pcounts);
		int words = 0;
		System.out.println("ptotal=" + ptotal);

		{
			long index = (RANDOM.nextLong() >>> 1) % ptotal;
			long sum = 0;
			for (int n = 0; n < pcounts.size(); n++) {
				sum = sum + pcounts.get(n);
				if (sum >= index) {
					List<Integer> p = plist.get(n);
					words = p.size();
					Collections.shuffle(p, RANDOM);
					for (Integer i : p) {
						String word = randomItem(dict.get(i));
						if (titleBox.isSelected()) {
							word = word.substring(0, 1).toUpperCase() + word.substring(1);
						}
						list.add(word);
					}
					break;
				}
			}
		}

		int dig = intValue(digitSpinner);
		if (dig > 0) {
			list.add(digit(dig));
		}

		int pun = intValue(punctSpinner);
		if (pun > 0) {
			list.add(punct(pun));
		}

		double space = ptotal + pow(10, dig) + pow(PUNCT, pun);

		if (shuffleBox.isSelected()) {
			Collections.shuffle(list, RANDOM);
			double f = fac(list.size()) / (fac(words)*fac(dig)*fac(pun));
			space = space * f;
		}

		setValue(String.join("", list), space);
	}

}
