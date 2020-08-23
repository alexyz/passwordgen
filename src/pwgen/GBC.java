package pwgen;

import java.awt.*;

public class GBC extends GridBagConstraints {
    /**
     * gridx/y=RELATIVE, gridw/h = 1, weightx/y = 0, anchor = CENTER, fill = NONE
     */
    public GBC() {
    }

    /**
     * gridx/y=x,y, gridw/h = 1, weightx/y = 0, anchor = CENTER, fill = NONE
     */
    public GBC(int x, int y) {
        gridx = x;
        gridy = y;
    }

    /**
     * insets all sides
     */
    public GBC inset(int i) {
        insets = new Insets(i, i, i, i);
        return this;
    }

    /**
     * insets trailing x only
     */
    public GBC insetRight(int right) {
        insets = new Insets(0, 0, 0, right);
        return this;
    }

    /**
     * insets trailing y only
     */
    public GBC insetBottom(int bottom) {
        insets = new Insets(0, 0, bottom, 0);
        return this;
    }

    /**
     * insets trailing x, y only
     */
    public GBC insetRB(int rb) {
        insets = new Insets(0, 0, rb, rb);
        return this;
    }

    /**
     * size (gridwidth/gridheight) (-1=rel, 0=rem, 1..n)
     */
    public GBC grid(int width, int height) {
        gridwidth = width;
        gridheight = height;
        return this;
    }

    /**
     * gridwidth REMAINDER, height 1
     */
    public GBC gridWidthRemainder() {
        return grid(REMAINDER, 1);
    }

    /**
     * width 1, height REMAINDER
     */
    public GBC gridHeightRemainder() {
        return grid(1, REMAINDER);
    }

    /**
     * width REMAINDER, height REMAINDER
     */
    public GBC gridRemainder() {
        return grid(REMAINDER, REMAINDER);
    }

    /**
     * weight
     */
    public GBC weight(double x, double y) {
        weightx = x;
        weighty = y;
        return this;
    }

    /**
     * weight 1, 0
     */
    public GBC weightX() {
        return weight(1, 0);
    }

    /**
     * weight 0, 1
     */
    public GBC weightY() {
        return weight(0, 1);
    }

    /**
     * weight 1, 1
     */
    public GBC weightXY() {
        return weight(1, 1);
    }

    /**
     * anchor
     */
    public GBC anchor(int v) {
        anchor = v;
        return this;
    }

    /**
     * fill horizontal
     */
    public GBC fillHorizontal() {
        fill = HORIZONTAL;
        return this;
    }

    /**
     * fill vertical
     */
    public GBC fillVertical() {
        fill = VERTICAL;
        return this;
    }

    /**
     * fill both
     */
    public GBC fillBoth() {
        fill = BOTH;
        return this;
    }
}
