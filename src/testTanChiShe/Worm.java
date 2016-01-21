package testTanChiShe;

import java.awt.*;
import java.util.Arrays;

public class Worm {
    private int currentDirection;
    // 蛇包含的格子
    private Cell[] cells;
    private Color color;
    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int RIGHT = 2;
    public static final int LEFT = -2;

    // 创建对象 创建默认的蛇：（0,0）（1,0）（2,0）••••••（11,0）
    public Worm() {// 构造器初始化对象
        color = Color.pink;// 蛇的颜色
        cells = new Cell[12];// 创建数组对象
        for (int x = 0, y = 0, i = 0; x < 12; x++) {
            // for(int y=0;;){}
            cells[i++] = new Cell(x, y, color);// 添加数组元素
        }
        currentDirection = DOWN;
    }

    public boolean contains(int x, int y) {
        // 数组迭代
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            if (cell.getX() == x && cell.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return Arrays.toString(cells);
    }

    public void creep() {
        for (int i = this.cells.length - 1; i >= 1; i--) {
            cells[i] = cells[i - 1];
        }

        cells[0] = createHead(currentDirection);
    }// 按照默认方法爬一步

    private Cell createHead(int direction) {// 根据方向，和当前（this）的头结点，创建新的头结点
        int x = cells[0].getX();
        int y = cells[0].getY();
        switch (direction) {
            case DOWN:
                y++;
                break;
            case UP:
                y--;
                break;
            case RIGHT:
                x++;
                break;
            case LEFT:
                x--;
                break;

        }
        return new Cell(x, y);
    }

    /**
     * food 食物
     *
     */
    public boolean creep(Cell food) {
        Cell head = createHead(currentDirection);
        boolean eat = head.getX() == food.getX() && head.getY() == food.getY();
        if (eat) {
            Cell[] ary = Arrays.copyOf(cells, cells.length + 1);
            cells = ary;// 丢弃原数组
        }
        for (int i = cells.length - 1; i >= 1; i--) {
            cells[i] = cells[i - 1];
        }
        cells[0] = head;
        return eat;
    }

    // 吃到东西就变长一格
    public boolean creep(int direction, Cell food) {

        if (currentDirection + direction == 0) {
            return false;
        }
        this.currentDirection = direction;
        Cell head = createHead(currentDirection);
        boolean eat = head.getX() == food.getX() && head.getY() == food.getY();
        if (eat) {
            Cell[] ary = Arrays.copyOf(cells, cells.length + 1);
            cells = ary;// 丢弃原数组
        }
        for (int i = cells.length - 1; i >= 1; i--) {
            cells[i] = cells[i - 1];
        }
        cells[0] = head;
        return eat;
    }

    // 检测在新的运动方向上是否能够碰到边界和自己（this 蛇）
    public boolean hit(int direction) {
        // 生成下个新头节点位置
        // 如果新头节点出界返回true，表示碰撞边界
        // •••••••••••••••
        if (currentDirection + direction == 0) {
            return false;
        }
        Cell head = createHead(direction);
        if (head.getX() < 0 || head.getX() >= WormStage.COLS || head.getY() < 0
                || head.getY() >= WormStage.ROWS) {
            return true;
        }
        for (int i = 0; i < cells.length - 1; i++) {
            if (cells[i].getX() == head.getX()
                    && cells[i].getY() == head.getY()) {
                return true;
            }
        }
        return false;
    }

    public boolean hit() {
        return hit(currentDirection);
    }

    // 为蛇添加会制方法
    // 利用来自舞台面板的画笔绘制蛇
    public void paint(Graphics g) {
        g.setColor(this.color);
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            g.fill3DRect(cell.getX() * WormStage.CELL_SIZE, cell.getY()
                            * WormStage.CELL_SIZE, WormStage.CELL_SIZE,
                    WormStage.CELL_SIZE, true);

        }
    }
}
