package pwgen;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;

import javax.swing.*;

public abstract class PasswordJPanel extends JPanel {
	
	protected static final SecureRandom secureRandom = new SecureRandom();
	
	protected final JPanel optionPanel = new JPanel();
	
	private final JTextField textField = new JTextField();
	private final JButton generateButton = new JButton("Generate");
	private final JButton copyButton = new JButton("Copy");
	
	public PasswordJPanel () {
		super(new GridLayout(2, 1));
		setBorder(BorderFactory.createEmptyBorder());
		
		textField.setColumns(20);
		textField.setFont(new Font("monospaced", Font.PLAIN, 16));
		textField.setBorder(BorderFactory.createEtchedBorder());
		
		generateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				textField.setText(generate());
			}
		});
		
		copyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
				clip.setContents(new StringSelection(textField.getText()), null);
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(textField);
		buttonPanel.add(generateButton);
		buttonPanel.add(copyButton);
		
		add(optionPanel);
		add(buttonPanel);
	}
	
	protected abstract String generate();
	
	protected abstract void loadPrefs ();
	
	protected abstract void savePrefs () throws Exception;
	
	/**
	 * return a printable ascii character
	 */
	protected String any (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			// don't include 0x7f (delete)
			// but do include space
			char c = (char) (secureRandom.nextInt(95) + 32);
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String lower (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('a' + secureRandom.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String upper (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('A' + secureRandom.nextInt(26));
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String digit (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			char c = (char) ('0' + secureRandom.nextInt(10));
			sb.append(c);
		}
		return sb.toString();
	}
	
	protected String punct (int max) {
		StringBuilder sb = new StringBuilder();
		for (int n = 0; n < max; n++) {
			while (true) {
				char c = (char) (secureRandom.nextInt(95) + 32);
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
			sb.append("01234567890abcdef".charAt(secureRandom.nextInt(16)));
		}
		return sb.toString();
	}
	
}
