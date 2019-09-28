package pwgen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.*;

public abstract class PasswordJPanel extends JPanel {

	private static final Font FONT = new Font("monospaced", Font.PLAIN, 16);

	protected final JPanel optionPanel = new JPanel();
	
	private final JTextField textField = new JTextField();
	private final JLabel bitsLabel = new JLabel("Bits");
	private final JTextField bitsField = new JTextField();
	private final JButton generateButton = new JButton("Generate");
	private final JButton copyButton = new JButton("Copy");
	
	public PasswordJPanel () {
		super(new GridLayout(2, 1));
		setBorder(BorderFactory.createEmptyBorder());
		
		textField.setFont(FONT);

		bitsField.setFont(FONT);
		bitsField.setEditable(false);
		bitsField.setColumns(4);

		generateButton.addActionListener(e -> generate());
		
		copyButton.addActionListener(e -> copy());
		
		JPanel buttonPanel = new JPanel(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5,5,5,5);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 4;
		c.weighty = 1;
		buttonPanel.add(textField, c);

		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		buttonPanel.add(bitsLabel, c);

		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0;
		buttonPanel.add(bitsField, c);

		c.gridx++;
		c.fill = GridBagConstraints.NONE;
		c.weightx = 0;
		buttonPanel.add(generateButton, c);

		c.gridx++;
		buttonPanel.add(copyButton, c);
		
		add(optionPanel);
		add(buttonPanel);
	}

	private void copy () {
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(new StringSelection(textField.getText()), null);
	}
	
	protected abstract void generate();

	protected void setValue (String pass, double size) {
		textField.setText(pass);
		bitsField.setText(size > 0 ? Integer.toString(PwUtil.log2(size)) : "");
	}
	
	protected abstract void loadPrefs () throws Exception;
	
	protected abstract void savePrefs () throws Exception;

}
