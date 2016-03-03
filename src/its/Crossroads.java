package its;

/**
 * 交通路口的信息和流量，包含4个方向，由四个方向到路口的流量
 * <p/>
 * created by C.L.Wang
 */
public class Crossroads {

    public static final int LEFT = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;

    private String mId; // 路口id
    private String[] mNeighbors; // 相邻的路口,顺序为左上右下

    private float[] mAddFlow; // 新增流量
    private float[] mTotalFlow; // 总体流量
    private float[] mCurFlow; // 当前流量

    private int mSetting; // 十字路口的状态

    // 路口的ID - 字符串
    public Crossroads(String id) {
        mId = id; // 路口的标记
        mNeighbors = new String[4]; // 四个相邻路口

        mTotalFlow = new float[4]; // 总流量
        mAddFlow = new float[4]; // 新增流量

        mCurFlow = new float[4]; // 当前流量
    }

    // 获取路口[direction]方向的新增流量
    public float getAddFlowOfDirection(int direction) {
        return mAddFlow[direction];
    }

    // 设置路口[direction]方向的新增流量
    public void setAddFlowOfDirection(int direction, float flow) {
        mAddFlow[direction] = flow;
    }

    // 根据新增流量添加12个方向的流量
    public void addCurFlow(float[] addFlows) {
        for (int i = 0; i < addFlows.length; ++i) {
            mCurFlow[i] += addFlows[i];
        }
    }

    // 设置0的总通过流量
    public float[] passCurFlowOfSettingZero() {

        final float fTotalThrough = (Constants.MAX_THROUGH_FLOW[0]
                + Constants.MAX_THROUGH_FLOW[1] + Constants.MAX_THROUGH_FLOW[2]);
        final float fRightThough = Constants.MAX_THROUGH_FLOW[2];

        // 左输入 - 左中右
        float left = mCurFlow[0] < fTotalThrough ? mCurFlow[0] : fTotalThrough;

        // 上输入 - 右
        float up = (mCurFlow[1] * Constants.TURN_PROB[2]) < fRightThough ?
                (mCurFlow[1] * Constants.TURN_PROB[2]) : fRightThough;

        // 右输入 - 左中右
        float right = mCurFlow[2] < fTotalThrough ? mCurFlow[2] : fTotalThrough;

        // 下输入 - 右
        float down = (mCurFlow[3] * Constants.TURN_PROB[2]) < fRightThough ?
                (mCurFlow[3] * Constants.TURN_PROB[2]) : fRightThough;

        float[] ret = new float[4];
        ret[0] = left;
        ret[1] = up;
        ret[2] = right;
        ret[3] = down;

        return ret;
    }

    // 设置1的总通过流量
    public float[] passCurFlowOfSettingOne() {

        final float fTotalThrough = (Constants.MAX_THROUGH_FLOW[0]
                + Constants.MAX_THROUGH_FLOW[1] + Constants.MAX_THROUGH_FLOW[2]);
        final float fRightThough = Constants.MAX_THROUGH_FLOW[2];

        // 左输入 - 右
        float left = (mCurFlow[0] * Constants.TURN_PROB[2]) < fRightThough ?
                (mCurFlow[0] * Constants.TURN_PROB[2]) : fRightThough;

        // 上输入 - 左中右
        float up = mCurFlow[1] < fTotalThrough ? mCurFlow[1] : fTotalThrough;

        // 右输入 - 右
        float right = (mCurFlow[2] * Constants.TURN_PROB[2]) < fRightThough ?
                (mCurFlow[2] * Constants.TURN_PROB[2]) : fRightThough;

        // 下输入 - 左中右
        float down = mCurFlow[3] < fTotalThrough ? mCurFlow[3] : fTotalThrough;

        float[] ret = new float[4];
        ret[0] = left;
        ret[1] = up;
        ret[2] = right;
        ret[3] = down;

        return ret;
    }

    // 根据设置[setting]更新4个路口的滞留流量
    public void updateCurFlow(int setting) {

        final float fTotalThrough = (Constants.MAX_THROUGH_FLOW[0]
                + Constants.MAX_THROUGH_FLOW[1] + Constants.MAX_THROUGH_FLOW[2]);
        final float fRightThough = Constants.MAX_THROUGH_FLOW[2];

        if (setting == 0) {

            mCurFlow[0] -= mCurFlow[0] < fTotalThrough ? mCurFlow[0] : fTotalThrough;
            mCurFlow[1] -= (mCurFlow[1] * Constants.TURN_PROB[2]) < fRightThough ?
                    (mCurFlow[1] * Constants.TURN_PROB[2]) : fRightThough;
            mCurFlow[2] -= mCurFlow[2] < fTotalThrough ? mCurFlow[2] : fTotalThrough;
            mCurFlow[3] -= (mCurFlow[3] * Constants.TURN_PROB[2]) < fRightThough ?
                    (mCurFlow[3] * Constants.TURN_PROB[2]) : fRightThough;

        } else if (setting == 1) {
            mCurFlow[0] -= (mCurFlow[0] * Constants.TURN_PROB[2]) < fRightThough ?
                    (mCurFlow[0] * Constants.TURN_PROB[2]) : fRightThough;
            mCurFlow[1] -= mCurFlow[1] < fTotalThrough ? mCurFlow[1] : fTotalThrough;
            mCurFlow[2] -= (mCurFlow[2] * Constants.TURN_PROB[2]) < fRightThough ?
                    (mCurFlow[2] * Constants.TURN_PROB[2]) : fRightThough;
            mCurFlow[3] -= mCurFlow[3] < fTotalThrough ? mCurFlow[3] : fTotalThrough;
        }

        if (setting == -1) { // 欺骗 - 起始时，可以全部通行，减少压力
            mCurFlow[0] -= mCurFlow[0] < fTotalThrough ? mCurFlow[0] : fTotalThrough;
            mCurFlow[1] -= mCurFlow[1] < fTotalThrough ? mCurFlow[1] : fTotalThrough;
            mCurFlow[2] -= mCurFlow[2] < fTotalThrough ? mCurFlow[2] : fTotalThrough;
            mCurFlow[3] -= mCurFlow[3] < fTotalThrough ? mCurFlow[3] : fTotalThrough;
        }
    }


    // 获取红绿灯状态
    public int getSetting() {
        return mSetting;
    }

    // 设置红绿灯状态
    public void setSetting(int setting) {
        mSetting = setting;
    }

    // 获取路口[direction]方向的总流量
    public float getTotalFlowOfDirection(int direction) {
        return mTotalFlow[direction];
    }

    // 增加路口[direction]方向的总流量
    public void addTotalFlowOfDirection(int direction, int flow) {
        mTotalFlow[direction] += flow;
    }

    // 返回所有的剩余流量
    public float getCurFlow() {
        return mCurFlow[0] + mCurFlow[1] + mCurFlow[2] + mCurFlow[3];
    }

    // 设置邻居
    public void setNeighbors(String left, String up, String right, String down) {
        mNeighbors[LEFT] = left;
        mNeighbors[UP] = up;
        mNeighbors[RIGHT] = right;
        mNeighbors[DOWN] = down;
    }

    // 根据标号返回邻居，左上右下[0-3]
    public String getNeighbor(int i) {
        return mNeighbors[i];
    }

    // 返回路口的名称
    public String getId() {
        return mId;
    }
}
