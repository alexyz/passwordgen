package pwgen;

import javax.swing.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.prefs.Preferences;

import static pwgen.PwUtil.RANDOM;

public class UuidPasswordJPanel extends PasswordJPanel {

    private final JCheckBox upperBox = new JCheckBox("Uppercase");
    private final JRadioButton randomButton = new JRadioButton("Random");
    private final JRadioButton timeButton = new JRadioButton("Time");
    private final ButtonGroup group = new ButtonGroup();
    private long nodeIdLong;

    public UuidPasswordJPanel() {
        optionPanel.add(upperBox);
        optionPanel.add(randomButton);
        optionPanel.add(timeButton);
        group.add(randomButton);
        group.add(timeButton);
    }

    @Override
    protected void loadPrefs() throws Exception {
        System.out.println("load prefs");
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        upperBox.setSelected(prefs.getBoolean("uidupper", false));
        nodeIdLong = prefs.getLong("uidnode", RANDOM.nextLong() * 0xffff_ffff_ffffL);
        randomButton.setSelected(prefs.getBoolean("uidrand", true));
        timeButton.setSelected(!randomButton.isSelected());
    }

    @Override
    protected void savePrefs() throws Exception {
        System.out.println("save prefs");
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.putBoolean("uidupper", upperBox.isSelected());
        prefs.putLong("uidnode", nodeIdLong);
        prefs.putBoolean("uidrand", randomButton.isSelected());
        prefs.flush();
    }

    @Override
    public void generate() {
        String v = randomButton.isSelected() ? UUID.randomUUID().toString() : genTimeUuid();
        String v2 = upperBox.isSelected() ? v.toUpperCase() : v.toLowerCase();
        int b = randomButton.isSelected() ? 121 : 14;
        setValue(v2, b, true);
    }

    private String genTimeUuid() {
        // 60 bit timestamp in 100ns interval since 15 October 1582
        long refTimeMs = refTime();
        long currentTimeMs = System.currentTimeMillis(); // current in ms since 1970
        long uuidTimeMs = currentTimeMs + (0L - refTimeMs);
        long uuidTime100ns = uuidTimeMs * 10_000;
//        System.out.println("unix time ms    = " + currentTimeMs);
//        System.out.println("uuid time ms    = " + uuidTimeMs);
//        System.out.println("uuid time 100ns = " + uuidTime100ns);

        // 32 bit low time
        BigInteger lowtime = BigInteger.valueOf(uuidTime100ns & 0xffff_ffffL);
        //System.out.println("lt=" + lowtime);
        // 16 bit mid time
        BigInteger midtime = BigInteger.valueOf((uuidTime100ns >>> 32) & 0xffffL);
        //System.out.println("mt=" + midtime);
        // 4 bit version
        BigInteger version = BigInteger.valueOf(1);
        //System.out.println("ver=" + version);
        // 12 bit high time
        BigInteger hightime = BigInteger.valueOf((uuidTime100ns >>> 48) & 0xfff);
        //System.out.println("ht=" + hightime);
        // 2 bit variant
        BigInteger variant = BigInteger.valueOf(0b10 & 0xf);
        //System.out.println("var=" + variant);
        // 14 bit sequence
        BigInteger sequence = BigInteger.valueOf(RANDOM.nextInt() & 0x3fff);
        //System.out.println("seq=" + sequence);
        // 48 bit node id (multicast)
        BigInteger node = BigInteger.valueOf((nodeIdLong & 0xffff_ffff_ffffL) | 0x0100_0000_0000L);
        //System.out.println("node=" + node);

        BigInteger i = lowtime.shiftLeft(16).or(midtime).shiftLeft(4).or(version).shiftLeft(12).or(hightime).shiftLeft(2).or(variant).shiftLeft(14).or(sequence).shiftLeft(48).or(node);

        StringBuilder sb = new StringBuilder();
        BigInteger m = BigInteger.valueOf(0xf);
        for (int n = 0; n < 32; n++) {
            if (n == 8 || n == 12 || n == 16 || n == 20) {
                sb.append("-");
            }
            sb.append(Integer.toHexString(i.shiftRight((31 - n) * 4).and(m).intValue()));
        }
        return sb.toString();
    }

    private long refTime() {
        Calendar cal = new GregorianCalendar(1582, 9, 15); // 1582 in ms since 1970
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        //System.out.println("uuid epoch str = " + sdf.format(new Date(cal.getTimeInMillis())));
        //System.out.println("uuid epoch     = " + cal.getTimeInMillis());
        // ISO/IEC 9834-8:2014 (E) reference constant
        //System.out.println("uuid reference = " + 0x01B21DD213814000L);
        return cal.getTimeInMillis();
    }
}
