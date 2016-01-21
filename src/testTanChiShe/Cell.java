package testTanChiShe;

import java.awt.*;

public class Cell {// 格子：食物或者蛇的节点  private int x;  private int y;   private Color color;// 颜色
    private int x;
    private int y;
    private Color color;// 颜色

    public Cell() {
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell(int x, int y, Color color) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "[" + x + "]" + "[" + y + "]";
    }
}