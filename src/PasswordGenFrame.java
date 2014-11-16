import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.*;

import java.util.*;

/**
 * simple secure password generator
 */
public class PasswordGenFrame extends JFrame {
	
	private static final String hex = "0123456789abcdef";
	private static final String alpha = "abcdefghijklmnopqrstuvwxyz";
	private static final int[] alphaCount = new int[26];
	private static int alphaSum;
	
	public static void main (String args[]) {
		alphaCount[0]=14142;
		alphaCount[1]=3812;
		alphaCount[2]=7412;
		alphaCount[3]=6841;
		alphaCount[4]=21761;
		alphaCount[5]=4480;
		alphaCount[6]=4019;
		alphaCount[7]=7366;
		alphaCount[8]=14693;
		alphaCount[9]=487;
		alphaCount[10]=1849;
		alphaCount[11]=8994;
		alphaCount[12]=4788;
		alphaCount[13]=14512;
		alphaCount[14]=12912;
		alphaCount[15]=4661;
		alphaCount[16]=165;
		alphaCount[17]=13057;
		alphaCount[18]=11582;
		alphaCount[19]=17052;
		alphaCount[20]=6021;
		alphaCount[21]=1747;
		alphaCount[22]=3815;
		alphaCount[23]=317;
		alphaCount[24]=2494;
		alphaCount[25]=150;
		for (int i : alphaCount) {
			alphaSum += i;
		}
		
		PasswordGenFrame f = new PasswordGenFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private final SecureRandom secureRandom = new SecureRandom();
	private final JTextArea textArea = new JTextArea();
	private final JSpinner weightedUpperSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final JSpinner weightedLowerSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 99, 1));
	private final JSpinner upperSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner lowerSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final JSpinner hexDigitSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner anySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JCheckBox shuffleBox = new JCheckBox("Shuffle");
	private final JButton generateButton = new JButton("Generate");
	private final JButton copyButton = new JButton("Copy");
	
	public PasswordGenFrame () {
		super("Password Generator");
		//shuffleBox.setSelected(true);
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				textArea.setText(generate());
			}
		});
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
				clip.setContents(new StringSelection(textArea.getText()), null);
			}
		});
		textArea.setColumns(20);
		textArea.setFont(new Font("monospaced", Font.PLAIN, 18));
		
		JPanel optionPanel = new JPanel();
		optionPanel.add(new JLabel("Weighted Upper"));
		optionPanel.add(weightedUpperSpinner);
		optionPanel.add(new JLabel("Weighted Lower"));
		optionPanel.add(weightedLowerSpinner);
		optionPanel.add(new JLabel("Upper"));
		optionPanel.add(upperSpinner);
		optionPanel.add(new JLabel("Lower"));
		optionPanel.add(lowerSpinner);
		
		JPanel optionPanel2 = new JPanel();
		optionPanel2.add(new JLabel("Digit"));
		optionPanel2.add(digitSpinner);
		optionPanel2.add(new JLabel("Hex Digit"));
		optionPanel2.add(hexDigitSpinner);
		optionPanel2.add(new JLabel("Punct"));
		optionPanel2.add(punctSpinner);
		optionPanel2.add(new JLabel("Any"));
		optionPanel2.add(anySpinner);
		optionPanel2.add(shuffleBox);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(textArea);
		buttonPanel.add(generateButton);
		buttonPanel.add(copyButton);
		
		JPanel contentPanel = new JPanel(new GridLayout(3, 1));
		contentPanel.add(optionPanel);
		contentPanel.add(optionPanel2);
		contentPanel.add(buttonPanel);
		
		setContentPane(contentPanel);
		pack();
	}
	
	public String generate () {
		List<Character> l = new ArrayList<Character>();
		for (int n = 0; n < ((Integer) weightedUpperSpinner.getValue()); n++) {
			l.add(Character.toUpperCase(weighted()));
		}
		for (int n = 0; n < ((Integer) weightedLowerSpinner.getValue()); n++) {
			l.add(weighted());
		}
		for (int n = 0; n < ((Integer) upperSpinner.getValue()); n++) {
			l.add(Character.toUpperCase(lower()));
		}
		for (int n = 0; n < ((Integer) lowerSpinner.getValue()); n++) {
			l.add(lower());
		}
		for (int n = 0; n < ((Integer) digitSpinner.getValue()); n++) {
			l.add(digit());
		}
		for (int n = 0; n < ((Integer) hexDigitSpinner.getValue()); n++) {
			l.add(hex());
		}
		for (int n = 0; n < ((Integer) punctSpinner.getValue()); n++) {
			l.add(punct());
		}
		for (int n = 0; n < ((Integer) anySpinner.getValue()); n++) {
			l.add(any());
		}
		if (shuffleBox.isSelected()) {
			Collections.shuffle(l, secureRandom);
		}
		StringBuilder sb = new StringBuilder();
		for (char c : l) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * return a printable ascii character
	 */
	private char any () {
		// don't include 0x7f (delete)
		// but do include space
		return (char) (' ' + secureRandom.nextInt(95));
	}
	
	private char lower () {
		return (char) ('a' + secureRandom.nextInt(26));
	}
	
	private char digit () {
		return (char) ('0' + secureRandom.nextInt(10));
	}
	
	private char punct () {
		char c;
		while (Character.isLetterOrDigit(c = any()) || Character.isWhitespace(c)) {
			//
		}
		return c;
	}
	
	private char weighted() {
		int t = secureRandom.nextInt(alphaSum);
		int s = 0;
		for (int n = 0; n < alphaCount.length; n++) {
			s += alphaCount[n];
			if (s > t) {
				return (char) ('a' + n);
			}
		}
		throw new RuntimeException();
	}
	
	private char hex () {
		return hex.charAt(secureRandom.nextInt(16));
	}
	
}
