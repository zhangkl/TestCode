package testTanChiShe;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.TimerTask;

public class WormStage extends JPanel {
    /** 舞台的列数 */
    public static final int COLS = 35;
    /** 舞台的行数 */
    public static final int ROWS = 35;
    /** 舞台格子的大小 */
    public static final int CELL_SIZE = 10;
    private Worm worm;
    private Cell food;
    private int count=0;

    public WormStage() {
        worm = new Worm();
        food = createFood();
    }

    /**
     * 随机生成食物，要避开蛇的身体 1 生成随机数 x, y 2 检查蛇是否包含(x,y)
     * 3 如果包含(x,y) 返回 1 4 创建食物节点
     * */
    private Cell createFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(COLS);// COLS列数
            y = random.nextInt(ROWS);// WOWS行数
        } while (worm.contains(x, y));
        return new Cell(x, y, Color.green);// 食物颜色
    }

    /** 初始化的舞台单元测试 */
    public static void test() {
        WormStage stage = new WormStage();
        System.out.println(stage.worm);
        System.out.println(stage.food);
    }

    /**
     * 重写JPanel绘制方法 paint:绘制，绘画，涂抹 Graphics 绘图，
     * 理解为：绑定到当前面板的画笔
     */
    public void paint(Graphics g) {
        // 添加自定义绘制！
        // 绘制背景
        g.setColor(Color.darkGray);// 背景色
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawString("分数：" + count, 20, 20);
        g.setColor(Color.cyan);// 边框上的颜色
        // draw 绘制 Rect矩形
        g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

        // 绘制食物
        g.setColor(food.getColor());
        // fill 填充 3D 3维 Rect矩形 突起的立体按钮形状
        g.fill3DRect(food.getX() * CELL_SIZE, food.getY() * CELL_SIZE,
                CELL_SIZE, CELL_SIZE, true);
        // 绘制蛇
        worm.paint(g);// 让蛇自己去利用画笔绘制
    }

    private java.util.Timer timer;

    /**
     * 启动定时器驱动蛇的运行 1 检查碰撞是否将要发生
     * 2 如果发生碰撞：创建新的蛇和食物，重写开始
     * 3 如果没有碰撞就爬行，并检查是否能够吃到食物
     * 4如果吃到食物：重新创建新的食物
     * 5 启动重新绘制界面功能 repaint() 更新界面显示效果！ repaint()
     * 方法会尽快调用paint(g) 更新界面！
     */
    private void go() {
        if (timer == null) {
            timer = new java.util.Timer();
        }
        timer.schedule(new TimerTask() {
            public void run() {
                if (worm.hit()) {// 如果蛇碰到边界或自己

                    worm = new Worm();// 创建新的蛇
                    food = createFood();// 创建新食物
                    count=0;
//                    System.exit(0);
                } else {// 如果没有碰到自己
                    boolean eat = worm.creep(food);
                    // 蛇向前(当前方向)爬行，返回结果表示是否吃到食物
                    if (eat) {// 如果吃到食物，就生成新食物
                        food = createFood();
                        count++;
                    }
                }
                repaint();
            }
        }, 0, 1000 / 20);
        this.requestFocus();
        this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_UP:
                        creepForFood(Worm.UP);
                        break;
                    case KeyEvent.VK_DOWN:
                        creepForFood(Worm.DOWN);
                        break;
                    case KeyEvent.VK_LEFT:
                        creepForFood(Worm.LEFT);
                        break;
                    case KeyEvent.VK_RIGHT:
                        System.out.println(123);
                        creepForFood(Worm.RIGHT);
                        break;
                    case KeyEvent.VK_SPACE:
                        try {
                            System.out.println(123);
                            timer.wait(100000);
                        } catch (Exception e1) {
                            System.out.println(e1.getMessage());
                            e1.printStackTrace();
                        }
                        break;
                }
            }

        });
    }

    private void creepForFood(int direction) {
        if (worm.hit(direction)) {
            worm = new Worm();
            food = createFood();
        } else {
            boolean eat = worm.creep(direction, food);
            if (eat) {
                food = createFood();
            }
        }
    }

    /** 软件启动的入口方法 */
    public static void main(String[] args) {
        // 启动软件....
        JFrame frame = new JFrame("贪吃蛇");// 一个画框对象
        frame.setSize(450, 480);// size 大小，setSize 设置大小
        // frame.setLocation(100,50);//Locationq位置
        frame.setLocationRelativeTo(null);// 居中
        // 设置默认的关闭操作为在关闭时候离开软件
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);// Visible可见的 设置可见性
        frame.setLayout(null);// 关闭默认布局管理，避免面板充满窗口

        WormStage stage = new WormStage();
        // System.out.println("CELL_SIZE * COLS:"+CELL_SIZE * COLS);
        stage.setSize(CELL_SIZE * COLS, CELL_SIZE * ROWS);
        stage.setLocation(40, 50);
        stage.setBorder(new LineBorder(Color.BLACK));
        frame.add(stage);// 在窗口添加舞台
        int choice = JOptionPane.showConfirmDialog(frame, "Are you Ready?",
                "提示", JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        switch (choice) {
            case JOptionPane.YES_OPTION:// 选择“是”
                stage.go();// 启动定时器驱动蛇自动运行
            case JOptionPane.NO_OPTION:// 选择“否”
                System.exit(0);// 退出
                break;
            case JOptionPane.CANCEL_OPTION:// 选择“取消”
                break;
        }
    }
}

