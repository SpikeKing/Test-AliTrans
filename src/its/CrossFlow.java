package its;

/**
 * 路口的流量通行情况,L代表左,R代表右,U代表上,D代表下
 * flowL2R代表从左边流向上边的流量，其他以此类推。
 * <p/>
 * created by C.L.Wang
 */
public class CrossFlow {

    public float flowL2R;
    public float flowL2U;
    public float flowL2D;

    public float flowU2D;
    public float flowU2R;
    public float flowU2L;

    public float flowR2L;
    public float flowR2D;
    public float flowR2U;

    public float flowD2U;
    public float flowD2L;
    public float flowD2R;

    public float getAll() {
        return flowL2R + flowL2U + flowL2D +
                flowU2D + flowU2R + flowU2L +
                flowR2L + flowR2D + flowR2U +
                flowD2U + flowD2L + flowD2R;
    }
}
