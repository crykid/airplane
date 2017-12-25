package airplane;

import airplane.run.ShootGame;

import java.awt.image.BufferedImage;

/**
 * Created by: blank with IntelliJ IDEA.
 * Created at: 2017-12-25 17:03
 * Description:
 */
public class Hero extends FlyingObject {

    //英雄机图片
    private BufferedImage[] images = {};
    //英雄机图片切换索引
    private int index = 0;
    //双倍火力
    private int doubleFire;
    //命
    private int life;

    public Hero() {
        life = 3;
        doubleFire = 0;
        images = new BufferedImage[]{ShootGame.hero0, ShootGame.hero1};
        image = ShootGame.hero0;
        width = image.getWidth();
        height = image.getHeight();
        x = 150;
        y = 400;
    }

    public int isDoubleFire() {
        return doubleFire;
    }

    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }

    public void addDoubleFire() {
        doubleFire = 40;
    }

    public void addLife() {
        life++;
    }

    public void subtractLife() {
        life--;
    }

    public int getLife() {
        return life;
    }

    /**
     * 当前物体移动了一下，相对距离，x,y鼠标位置
     */
    public void moveTo(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }

    @Override
    public boolean outOfBounds() {
        return false;
    }

    public Bullet[] shoot() {
        int xStep = width / 4;
        int yStep = 20;
        if (doubleFire > 0) {
            //双倍火力
            Bullet[] bullets = new Bullet[2];
            bullets[0] = new Bullet(x + xStep, y - yStep);
            //y-yStep(子弹距飞机的位置)
            bullets[1] = new Bullet(x + 3 * xStep, y - yStep);
            return bullets;
        } else {
            //单倍火力
            Bullet[] bullets = new Bullet[1];
            bullets[0] = new Bullet(x + 2 * xStep, y - yStep);
            return bullets;
        }
    }

    @Override
    public void step() {
        if (images.length > 0) {
            //切换图片hero0，hero1
            image = images[index++ / 10 % images.length];

        }
    }

    /**
     * 碰撞算法
     */
    public boolean hit(FlyingObject other) {
        int x1 = other.x - this.width / 2;
        //x坐标最小距离
        int x2 = other.x + this.width / 2 + other.width;
        // x坐标最大距离
        int y1 = other.y - this.height / 2;
        // y坐标最小距离
        int y2 = other.y + this.height / 2 + other.height;
        // y坐标最大距离
        int herox = this.x + this.width / 2;
        // 英雄机x坐标中心点距离
        int heroy = this.y + this.height / 2;
        // 英雄机y坐标中心点距离
        return herox > x1 && herox < x2 && heroy > y1 && heroy < y2;
        // 区间范围内为撞上了
    }
}
