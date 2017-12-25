package airplane;

import airplane.function.Enemy;
import airplane.run.ShootGame;

import java.util.Random;

/**
 * Created by: blank with IntelliJ IDEA.
 * Created at: 2017-12-25 16:43
 * Description:
 */
public class Airplane extends FlyingObject implements Enemy {
    private int speed = 3;

    public Airplane() {
        this.image = ShootGame.airplane;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rand = new Random();
        x = rand.nextInt(ShootGame.WIDTH - width);

    }

    @Override
    public boolean outOfBounds() {
        return y > ShootGame.HEIGHT;
    }

    @Override
    public void step() {
        y += speed;
    }

    /**
     * 获取分数
     *
     * @return
     */
    @Override
    public int getScore() {
        return 5;
    }
}
