package pwgen;

import javax.swing.*;
import java.security.SecureRandom;
import java.util.Base64;

public class PwUtil {

    protected static final SecureRandom RANDOM = new SecureRandom();
    protected static final int PUNCT = punctCount();
    protected static final int ANY = 126 - 33;

    /** int value from spinner */
    public static int intValue(JSpinner s) {
        return ((Number) s.getValue()).intValue();
    }

    /** generate random integer in range */
    public static int randomInt (int greaterThanOrEqual, int lessThan) {
        return RANDOM.nextInt(lessThan - greaterThanOrEqual) + greaterThanOrEqual;
    }

    /** return x to the power of y */
    public static double pow (int x, int y) {
        return Math.pow(x, y);
    }

    /** return log2 of v */
    public static int log2 (double v) {
        return (int) (Math.log(v) / Math.log(2));
    }

    /** return factorial of i */
    public static double fac (int i) {
        double v = 1;
        for (int n = 1; n <= i; n++) {
            v = v * n;
        }
        return v;
    }

    private static int punctCount() {
        int c = 0;
        for (int n = 32; n < 127; n++) {
            char ch = (char) n;
            if (!Character.isWhitespace(ch) && !Character.isLetterOrDigit(ch)) {
                c++;
            }
        }
        return c;
    }

    /** pick random item from list */
    public static <T> T randomItem(java.util.List<T> l) {
        return l.get(RANDOM.nextInt(l.size()));
    }

    /**
     * return a sequence of any ascii characters
     */
    public static String any (int count) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < count; n++) {
            // don't include 0x7f (delete) or space
            char c = (char) randomInt(33, 127);
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * return string of lowercase ascii characters
     */
    public static String lower (int count) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < count; n++) {
            char c = (char) ('a' + RANDOM.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * return string of uppercase ascii characters
     */
    public static String upper (int count) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < count; n++) {
            char c = (char) ('A' + RANDOM.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * return string of decimal ascii characters
     */
    public static String digit (int count) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < count; n++) {
            char c = (char) ('0' + RANDOM.nextInt(10));
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * return string of punctuation ascii characters
     */
    public static String punct (int count) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < count; n++) {
            while (true) {
                char c = (char) randomInt(33, 127);
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }

    public static String hex (int max) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < max; n++) {
            sb.append(Integer.toHexString(RANDOM.nextInt(16)));
        }
        return sb.toString();
    }

    /** generate random bits as hex or base64 string */
    public static String bits (int bits, boolean b64) {
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

    private PwUtil() {

    }
}
