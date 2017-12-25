package airplane.run;

import airplane.*;
import airplane.function.Award;
import airplane.function.Enemy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

import static java.awt.Event.PAUSE;

/**
 * Created by: blank with IntelliJ IDEA.
 * Created at: 2017-12-25 16:55
 * Description: 游戏入口
 */
public class ShootGame extends JPanel {
    //面板宽
    public static final int WIDTH = 400;
    public static final int HEIGHT = 654;

    /**
     * 游戏的当前状态: START RUNNING PAUSE GAME_OVER
     */
    private int state;

    private static final int START = 0;

    private static final int RUNNING = 1;
    private static final int PAUS = 2;
    private static final int GAME_OVER = 3;
    //分数
    private int score = 0;
    //计时器
    private Timer timer;
    // 时间间隔(毫秒)
    private int intervel = 1000 / 100;
    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage airplane;
    public static BufferedImage bee;
    public static BufferedImage bullet;
    public static BufferedImage hero0;
    public static BufferedImage hero1;
    public static BufferedImage pause;
    public static BufferedImage gameover;

    // 敌机数组
    private FlyingObject[] flyings = {};
    private Bullet[] bullets = {};

    private Hero hero = new Hero();

    static {
        try {
            background = ImageIO.read(ShootGame.class.getResource("../media/background.png"));
            start = ImageIO.read(ShootGame.class.getResource("../media/start.png"));
            airplane = ImageIO.read(ShootGame.class.getResource("../media/airplane.png"));
            bee = ImageIO.read(ShootGame.class.getResource("../media/bee.png"));
            bullet = ImageIO.read(ShootGame.class.getResource("../media/bullet.png"));
            hero0 = ImageIO.read(ShootGame.class.getResource("../media/hero0.png"));
            pause = ImageIO.read(ShootGame.class.getResource("../media/pause.png"));
            gameover = ImageIO.read(ShootGame.class.getResource("../media/gameover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(background, 0, 0, null);
        // 画背景图
        paintHero(g);
        // 画英雄机
        paintBullets(g);
        // 画子弹
        paintFlyingObjects(g);
        // 画飞行物
        paintScore(g);
        // 画分数
        paintState(g);
    }

    /**
     * 画英雄机
     */
    public void paintHero(Graphics g) {
        g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
    }

    /**
     * 画子弹
     */
    public void paintBullets(Graphics g) {
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            g.drawImage(b.getImage(), b.getX() - b.getWidth() / 2, b.getY(), null);
        }
    }

    /**
     * 画飞行物
     */
    public void paintFlyingObjects(Graphics g) {
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            g.drawImage(f.getImage(), f.getX(), f.getY(), null);
        }
    }

    /**
     * 画分数
     */
    public void paintScore(Graphics g) {
        int x = 10; // x坐标
        int y = 25; // y坐标
        // 字体
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 22);
        // 设置字体
        g.setColor(new Color(0xFF0000));
        // 画分数
        g.setFont(font);
        g.drawString("SCORE:" + score, x, y);
        // y坐标增20
        y = y + 20;
        // 画命
        g.drawString("LIFE:" + hero.getLife(), x, y);
    }

    /**
     * 画游戏状态
     */
    public void paintState(Graphics g) {
        switch (state) {
            case START: // 启动状态
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE: // 暂停状态
                g.drawImage(pause, 0, 0, null);
                break;

            case GAME_OVER: // 游戏终止状态
                g.drawImage(gameover, 0, 0, null);
                break;
        }
    }

    public static void main(String[] args) {
        // 面板对象
        JFrame frame = new JFrame("Fly");
        ShootGame game = new ShootGame();
        // 将面板添加到JFrame中
        frame.add(game);
        //设置大小
        frame.setSize(WIDTH, HEIGHT);
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗体的图标
        frame.setIconImage(new ImageIcon("images/icon.jpg").getImage());
        // 尽快调用paint
        frame.setLocationRelativeTo(null);
        // 设置窗体初始位置
        frame.setVisible(true);
        // 启动执行
        game.action();
    }

    /**
     * 启动执行代码
     */
    public void action() { // 鼠标监听事件
        MouseAdapter l = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // 鼠标移动
                if (state == RUNNING) {
                    //运行状态下移动英雄机--随鼠标位置
                    int x = e.getX();
                    int y = e.getY();
                    hero.moveTo(x, y);
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // 鼠标进入
                if (state == PAUSE) {
                    // 暂停状态下运行
                    state = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 鼠标退出
                if (state == RUNNING) {
                    // 游戏未结束，则设置其为暂停
                    state = PAUSE;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标点击
                switch (state) {
                    case START:
                        // 启动状态下运行
                        state = RUNNING;
                        break;
                    case GAME_OVER: // 游戏结束，清理现场
                        // 清空飞行物
                        flyings = new FlyingObject[0];
                        // 清空子弹
                        bullets = new Bullet[0];
                        // 重新创建英雄机
                        hero = new Hero();
                        //清空成绩
                        score = 0;
                        // 状态设置为启动
                        state = START;
                        break;
                }
            }

        };
        // 处理鼠标点击操作
        this.addMouseListener(l);
        // 处理鼠标滑动操作
        this.addMouseMotionListener(l);
        // 主流程控制
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 运行状态
                if (state == RUNNING) {
                    // 飞行物入场
                    enterAction();
                    // 走一步
                    stepAction();
                    // 英雄机射击
                    shootAction();
                    // 子弹打飞行物
                    bangAction();
                    // 删除越界飞行物及子弹
                    outOfBoundsAction();
                    // 检查游戏结束
                    checkGameOverAction();
                }
                // 重绘，调用paint()方法
                repaint();
            }
        }, intervel, intervel);
    }

    int flyEnteredIndex = 0;

    // 飞行物入场计数
    // /** 飞行物入场 */
    public void enterAction() {
        flyEnteredIndex++;
        if (flyEnteredIndex % 40 == 0) {
            // 400毫秒生成一个飞行物--10*40
            FlyingObject obj = nextOne();
            // 随机生成一个飞行物
            flyings = Arrays.copyOf(flyings, flyings.length + 1);
            flyings[flyings.length - 1] = obj;
        }
    }

    /**
     * 走一步
     */
    public void stepAction() {
        for (int i = 0; i < flyings.length; i++) {
            // 飞行物走一步
            FlyingObject f = flyings[i];
            f.step();
        }
        for (int i = 0; i < bullets.length; i++) {
            // 子弹走一步
            Bullet b = bullets[i];
            b.step();
        }
        // 英雄机走一步 }
        hero.step();

    }

    /**
     * 飞行物走一步
     */
    public void flyingStepAction() {
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            f.step();
        }
    }

    // 射击计数
    int shootIndex = 0;

    /**
     * 射击
     */
    public void shootAction() {
        shootIndex++;
        // 300毫秒发一颗
        if (shootIndex % 30 == 0) {
            // 英雄打出子弹
            Bullet[] bs = hero.shoot();
            // 扩容
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
            // 追加数组
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
        }
    }

    /**
     * 子弹与飞行物碰撞检测
     */
    public void bangAction() {
        // 遍历所有子弹
        for (int i = 0; i < bullets.length; i++) {
            // 子弹和飞行物之间的碰撞检查
            Bullet b = bullets[i];
            bang(b);
        }
    }

    /**
     * 删除越界飞行物及子弹
     */
    public void outOfBoundsAction() {
        int index = 0;
        // 索引
        FlyingObject[] flyingLives = new FlyingObject[flyings.length];
        // 活着的飞行物
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject f = flyings[i];
            if (!f.outOfBounds()) {
                flyingLives[index++] = f;
                // 不越界的留着
            }
        }
        flyings = Arrays.copyOf(flyingLives, index);
        // 将不越界的飞行物都留着
        index = 0;
        // 索引重置为0
        Bullet[] bulletLives = new Bullet[bullets.length];
        for (int i = 0; i < bullets.length; i++) {
            Bullet b = bullets[i];
            if (!b.outOfBounds()) {
                bulletLives[index++] = b;
            }
        }
        bullets = Arrays.copyOf(bulletLives, index);
        // 将不越界的子弹留着
    }

    /**
     * 检查游戏结束
     */
    public void checkGameOverAction() {
        if (isGameOver() == true) {
            state = GAME_OVER;
            // 改变状态
        }
    }

    /**
     * 检查游戏是否结束
     */
    public boolean isGameOver() {
        for (int i = 0; i < flyings.length; i++) {
            int index = -1;
            FlyingObject obj = flyings[i];
            if (hero.hit(obj)) {
                // 检查英雄机与飞行物是否碰撞
                hero.subtractLife();
                // 减命
                hero.setDoubleFire(0);
                // 双倍火力解除
                index = i;
                // 记录碰上的飞行物索引
            }
            if (index != -1) {
                FlyingObject t = flyings[index];
                flyings[index] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = t;
                // 碰上的与最后一个飞行物交换
                flyings = Arrays.copyOf(flyings, flyings.length - 1);
                // 删除碰上的飞行物
            }
        }
        return hero.getLife() <= 0;
    }

    /**
     * 子弹和飞行物之间的碰撞检查
     */
    public void bang(Bullet bullet) {
        int index = -1;
        // 击中的飞行物索引
        for (int i = 0; i < flyings.length; i++) {
            FlyingObject obj = flyings[i];
            if (obj.shootBy(bullet)) {
                // 判断是否击中
                index = i;
                // 记录被击中的飞行物的索引
                break;
            }
        }
        // 有击中的飞行物
        if (index != -1) {
            // 记录被击中的飞行物
            FlyingObject one = flyings[index];
            // 被击中的飞行物与最后一个飞行物交换
            FlyingObject temp = flyings[index];
            flyings[index] = flyings[flyings.length - 1];
            // 删除最后一个飞行物(即被击中的)
            flyings[flyings.length - 1] = temp;
            // 检查one的类型(敌人加分，奖励获取)
            flyings = Arrays.copyOf(flyings, flyings.length - 1);
            // 检查类型，是敌人，则加分
            if (one instanceof Enemy) {
                // 强制类型转换
                Enemy e = (Enemy) one;
                // 加分
                score += e.getScore();
                // 若为奖励，设置奖励 '
            } else {
                Award a = (Award) one;
                // 获取奖励类型
                int type = a.getType();
                switch (type) {
                    case Award.DOUBLE_FIRE:
                        // 设置双倍火力
                        hero.addDoubleFire();
                        break;
                    case Award.LIFE:
                        // 设置加命
                        hero.addLife();
                        break;
                }
            }
        }
    }

    /**
     * 随机生成飞行物 * * @return 飞行物对象
     */
    public static FlyingObject nextOne() {
        Random random = new Random();
        int type = random.nextInt(20);
        // [0,20)
        if (type < 4) {
            return new Bee();
        } else {
            return new Airplane();
        }
    }
}
