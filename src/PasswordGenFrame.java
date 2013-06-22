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
	
	public static void main (String args[]) {
		PasswordGenFrame f = new PasswordGenFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
	private final SecureRandom secureRandom = new SecureRandom();
	private final JTextArea textArea = new JTextArea();
	private final JSpinner lowerSpinner = new JSpinner(new SpinnerNumberModel(6, 0, 99, 1));
	private final JSpinner upperSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final JSpinner digitSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 99, 1));
	private final JSpinner punctSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JSpinner anySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 99, 1));
	private final JButton generateButton = new JButton("Generate");
	private final JButton copyButton = new JButton("Copy");
	
	public PasswordGenFrame () {
		super("Password Generator");
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
		optionPanel.add(new JLabel("Lower"));
		optionPanel.add(lowerSpinner);
		optionPanel.add(new JLabel("Upper"));
		optionPanel.add(upperSpinner);
		optionPanel.add(new JLabel("Digit"));
		optionPanel.add(digitSpinner);
		optionPanel.add(new JLabel("Punct"));
		optionPanel.add(punctSpinner);
		optionPanel.add(new JLabel("Any"));
		optionPanel.add(anySpinner);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(textArea);
		buttonPanel.add(generateButton);
		buttonPanel.add(copyButton);
		
		JPanel contentPanel = new JPanel(new GridLayout(2, 1));;
		contentPanel.add(optionPanel);
		contentPanel.add(buttonPanel);
		
		setContentPane(contentPanel);
		pack();
	}
	
	public String generate () {
		List<Character> l = new ArrayList<Character>();
		for (int n = 0; n < ((Integer) lowerSpinner.getValue()); n++) {
			l.add(lower());
		}
		for (int n = 0; n < ((Integer) upperSpinner.getValue()); n++) {
			l.add(upper());
		}
		for (int n = 0; n < ((Integer) digitSpinner.getValue()); n++) {
			l.add(digit());
		}
		for (int n = 0; n < ((Integer) punctSpinner.getValue()); n++) {
			l.add(punct());
		}
		for (int n = 0; n < ((Integer) anySpinner.getValue()); n++) {
			l.add(any());
		}
		Collections.shuffle(l, secureRandom);
		StringBuilder sb = new StringBuilder();
		for (char c : l) {
			sb.append(c);
		}
		return sb.toString();
	}
	
	char any () {
		return (char) (secureRandom.nextInt(95) + 33);
	}
	
	char lower () {
		char c;
		while (!Character.isLowerCase(c = any()));
		return c;
	}
	
	char upper () {
		char c;
		while (!Character.isUpperCase(c = any()));
		return c;
	}
	
	char digit () {
		char c;
		while (!Character.isDigit(c = any()));
		return c;
	}
	
	char punct () {
		char c;
		while (Character.isAlphabetic(c = any()) || Character.isDigit(c));
		return c;
	}
}
