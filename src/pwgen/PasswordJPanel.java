package pwgen;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.security.SecureRandom;
import java.util.Base64;

import javax.swing.*;

public abstract class PasswordJPanel extends JPanel {
	
	protected static final SecureRandom RANDOM = new SecureRandom();
	
	protected final JPanel optionPanel = new JPanel();
	
	private final JTextField textField = new JTextField();
	private final JButton generateButton = new JButton("Generate");
	private final JButton copyButton = new JButton("Copy");
	
	public PasswordJPanel () {
		super(new GridLayout(2, 1));
		setBorder(BorderFactory.createEmptyBorder());
		
		textField.setColumns(32);
		textField.setFont(new Font("monospaced", Font.PLAIN, 16));
		textField.setBorder(BorderFactory.createEtchedBorder());
		
		generateButton.addActionListener(e -> textField.setText(generate()));
		
		copyButton.addActionListener(e -> copy());
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(textField);
		buttonPanel.add(generateButton);
		buttonPanel.add(copyButton);
		
		add(optionPanel);
		add(buttonPanel);
	}

	private void copy () {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(new StringSelection(textField.getText()), null);
	}
	
	protected abstract String generate();
	
	protected abstract void loadPrefs () throws Exception;
	
	protected abstract void savePrefs () throws Exception;
	
	/**
	 * return a printable ascii character
	 */
	protected String any (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			// don't include 0x7f (delete)
			// but do include space
			char c = (char) (RANDOM.nextInt(95) + 32);
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String lower (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('a' + RANDOM.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String upper (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('A' + RANDOM.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String digit (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('0' + RANDOM.nextInt(10));
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String punct (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			while (true) {
				char c = (char) (RANDOM.nextInt(95) + 32);
				if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}
	
	protected String hex (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			sb.append(Integer.toHexString(RANDOM.nextInt(16)));
		}
		return sb.toString();
	}
	
	protected String bits (int bits, boolean b64) {
		byte[] a = new byte[bits/8];
		RANDOM.nextBytes(a);
		if (b64) {
			return Base64.getEncoder().encodeToString(a);
		} else {
			StringBuilder sb = new StringBuilder();
			for (int n = 0; n < a.length; n++) {
				byte b = a[n];
				sb.append(Integer.toHexString(b&0xf)).append(Integer.toHexString((b>>4)&0xf));
			}
			return sb.toString();
		}
	}
	
}
