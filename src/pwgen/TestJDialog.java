package pwgen;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class TestJDialog extends JDialog {

    private char[] pass;

    public static void main(String[] args) {
        TestJDialog f = new TestJDialog(null, "password".toCharArray());
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private final JPasswordField passField = new JPasswordField();
    private final JTextField correctField = new JTextField();
    private final JTextField incorrectField = new JTextField();
    private final JButton closeButton = new JButton("Close");
    private int correct, incorrect;

    public TestJDialog(Window w, char[] pass) {
        super(w, "Test");
        this.pass = pass;

        JPanel p = new JPanel(new GridBagLayout());

        passField.addActionListener(e -> test());
        p.add(passField, new GBC(0, 0).gridWidthRemainder().fillHorizontal().inset(5));

        p.add(new JLabel("Correct"), new GBC(0, 1).inset(5));

        correctField.setColumns(4);
        correctField.setFont(PasswordJPanel.FONT);
        correctField.setEditable(false);
        p.add(correctField, new GBC(1, 1).insetRB(5));

        p.add(new JLabel("Incorrect"), new GBC(2, 1).insetRB(5));

        incorrectField.setColumns(4);
        incorrectField.setFont(PasswordJPanel.FONT);
        incorrectField.setEditable(false);
        p.add(incorrectField, new GBC(3, 1).insetRB(5));

        closeButton.addActionListener(e -> close());
        p.add(closeButton, new GBC(4, 1).insetRB(5));

        setTitle("Password Test");
        setContentPane(p);
        pack();
    }

    private void close() {
        Arrays.fill(pass, ' ');
        setVisible(false);
    }

    private void test() {
        char[] a = passField.getPassword();
        if (a.length > 0) {
            if (Arrays.equals(a, pass)) {
                correct++;
            } else {
                incorrect++;
            }
            passField.setText("");
            Arrays.fill(a, ' ');
        }
        updateCount();
    }

    private void updateCount() {
        correctField.setText(String.valueOf(correct));
        incorrectField.setText(String.valueOf(incorrect));
        repaint();
    }
}
