package pwgen;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.swing.*;

public abstract class PasswordJPanel extends JPanel {

    public static final Font FONT = new Font("monospaced", Font.PLAIN, 16);

    protected final JPanel optionPanel = new JPanel();

    private final JTextField textField = new JTextField();
    private final JTextField bitsField = new JTextField();
    private final JButton generateButton = new JButton("Generate");
    private final JButton copyButton = new JButton("Copy");
    private final JButton testButton = new JButton("Test");

    public PasswordJPanel() {
        super(new GridLayout(2, 1));
        setBorder(BorderFactory.createEmptyBorder());

        textField.setFont(FONT);

        bitsField.setFont(FONT);
        bitsField.setEditable(false);
        bitsField.setColumns(4);

        generateButton.addActionListener(e -> generate());

        copyButton.addActionListener(e -> copy());

        testButton.addActionListener(e -> test());

        JPanel p = new JPanel(new GridBagLayout());
        p.add(textField, new GBC(0, 0).inset(5).fillHorizontal().weight(4, 1));
        p.add(new JLabel("Bits"), new GBC(1, 0).insetRB(5));
        p.add(bitsField, new GBC(2, 0).insetRB(5).fillHorizontal());
        p.add(generateButton, new GBC(3, 0).insetRB(5));
        p.add(copyButton, new GBC(4, 0).insetRB(5));
        p.add(testButton, new GBC(5, 0).insetRB(5));

        add(optionPanel);
        add(p);
    }

    protected void test() {
        String t = textField.getText();
        if (t.length() > 0) {
            TestJDialog p = new TestJDialog(SwingUtilities.getWindowAncestor(this), t.toCharArray());
            p.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            p.setLocationRelativeTo(this);
            p.setVisible(true);
        }
    }

    private void copy() {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(new StringSelection(textField.getText()), null);
    }

    protected abstract void generate();

    protected void setValue(String pass, double size) {
        textField.setText(pass);
        bitsField.setText(size > 0 ? Integer.toString(PwUtil.log2(size)) : "");
    }

    protected abstract void loadPrefs() throws Exception;

    protected abstract void savePrefs() throws Exception;

}
