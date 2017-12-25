package airplane;

import airplane.run.ShootGame;

public class Bullet extends FlyingObject {
    //移动速度
    private int speed = 3;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = ShootGame.bullet;
    }

    /**
     * 越界处理
     */
    @Override
    public boolean outOfBounds() {
        return y < -height;
    }

    /**
     * 移速
     */
    @Override
    public void step() {
        y -= speed;

    }
}
