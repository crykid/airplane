package airplane.function;

/**
 * Created by: blank with IntelliJ IDEA.
 * Created at: 2017-12-25 16:53
 * Description:奖励
 */
public interface Award {
    //双倍火力
    int DOUBLE_FIRE = 0;
    //一条命
    int LIFE = 1;

    /**
     * 获取奖励类型（上面的0或者1）
     */
    int getType();
}
