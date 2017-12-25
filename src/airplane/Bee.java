package airplane;

import airplane.function.Award;
import airplane.run.ShootGame;

import java.util.Random;

/**
 * Created by: blank with IntelliJ IDEA.
 * Created at: 2017-12-25 16:56
 * Description: 小蜜蜂
 */
public class Bee extends FlyingObject implements Award {
    private int xSpeed = 1;
    private int ySpeed = 2;

    private int awardType;


    public Bee() {
        this.image = ShootGame.bee;
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random ran = new Random();
        x = ran.nextInt(ShootGame.WIDTH - width);
        awardType = ran.nextInt(2);

    }



    @Override
    public boolean outOfBounds() {
        return y > ShootGame.HEIGHT;
    }

    @Override
    public void step() {
        x += xSpeed;
        y += ySpeed;
        if (x > ShootGame.WIDTH - width)
            xSpeed = -1;
        if (x < 0)
            xSpeed = 1;
    }


    @Override
    public int getType() {
        return awardType;
    }
}
