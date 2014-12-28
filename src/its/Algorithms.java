package its;

import java.util.*;

public class Algorithms {

    /**
     * 将现有流量复制一份
     *
     * @param flow 流量
     * @return a copy
     */
    public static Map<String, float[]> CopyFlow(Map<String, float[]> flow) {
        Map<String, float[]> ret = new HashMap<String, float[]>();

        for (Map.Entry<String, float[]> entry : flow.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().clone());
        }

        return ret;
    }

    /**
     * 根据概率计算流量
     *
     * @param x    - 流入量
     * @param prob - 概率alpha,beta,gamma
     * @return 流量
     */
    private static int[] getPredictFlow(int x, float[] prob) {
        int[] ret = new int[prob.length];
        for (int i = 0; i < prob.length; i++) {
            ret[i] = (int) Math.ceil(((float) x) * prob[i]);
        }

        return ret;
    }

    /**
     * 根据当前四个方向的流量，和转弯概率计算出车辆流动情况
     *
     * @param trafficStatus - 四个方向的流量
     * @return 车辆流动情况
     */
    public static CrossFlow calCrossFlow(int[] trafficStatus) {

        // 转弯概率左0.1, 中0.8, 右0.1
        float[] turnProb = Constants.TURN_PROB;

        CrossFlow flow = new CrossFlow();

        /**
         * 和最大流量比较，选择单次通过量
         */
        int[] t = getPredictFlow(trafficStatus[0], turnProb);
        flow.flowL2U = Math.min((int) Constants.MAX_THROUGH_FLOW[0], t[0]);
        flow.flowL2R = Math.min((int) Constants.MAX_THROUGH_FLOW[1], t[1]);
        flow.flowL2D = Math.min((int) Constants.MAX_THROUGH_FLOW[2], t[2]);

        t = getPredictFlow(trafficStatus[1], turnProb);
        flow.flowU2R = Math.min((int) Constants.MAX_THROUGH_FLOW[0], t[0]);
        flow.flowU2D = Math.min((int) Constants.MAX_THROUGH_FLOW[1], t[1]);
        flow.flowU2L = Math.min((int) Constants.MAX_THROUGH_FLOW[2], t[2]);

        t = getPredictFlow(trafficStatus[2], turnProb);
        flow.flowR2D = Math.min((int) Constants.MAX_THROUGH_FLOW[0], t[0]);
        flow.flowR2L = Math.min((int) Constants.MAX_THROUGH_FLOW[1], t[1]);
        flow.flowR2U = Math.min((int) Constants.MAX_THROUGH_FLOW[2], t[2]);

        t = getPredictFlow(trafficStatus[3], turnProb);
        flow.flowD2L = Math.min((int) Constants.MAX_THROUGH_FLOW[0], t[0]);
        flow.flowD2U = Math.min((int) Constants.MAX_THROUGH_FLOW[1], t[1]);
        flow.flowD2R = Math.min((int) Constants.MAX_THROUGH_FLOW[2], t[2]);

        return flow;
    }

    /**
     * 计算往前走的cost
     *
     * @param ecost - 根据前馈反馈算法计算出的边的权重
     * @param ef    - 车辆流动情况
     * @return - 第一种状态和第二种状态的cost
     */
    public static float[] JudgeForwardCost(float[] ecost, CrossFlow ef) {
        float[][] forwardCost = new float[2][];
        forwardCost[0] = new float[4];
        forwardCost[1] = new float[4];
        forwardCost[0][0] = ef.flowR2L + ef.flowU2L;
        forwardCost[0][1] = ef.flowL2U + ef.flowR2U;
        forwardCost[0][2] = ef.flowL2R + ef.flowD2R;
        forwardCost[0][3] = ef.flowL2D + ef.flowR2D;

        forwardCost[1][0] = ef.flowU2L + ef.flowD2L;
        forwardCost[1][1] = ef.flowD2U + ef.flowR2U;
        forwardCost[1][2] = ef.flowU2R + ef.flowD2R;
        forwardCost[1][3] = ef.flowU2D + ef.flowL2D;

        float[] ret = new float[2];

        for (int i = 0; i < 4; i++) {
            ret[0] += forwardCost[0][i] * ecost[i];
            ret[1] += forwardCost[1][i] * ecost[i];
        }

        return ret;
    }

    /**
     * 计算第一种状态和第二种状态的cost
     *
     * @param flow  - 当前4个方向的流量
     * @param ecost - 根据前馈反馈算法计算出的边的权重
     * @param ef    - 车辆流动情况
     * @return 第一种状态和第二种状态的cost
     */
    public static float[] JudgeCost(int[] flow, float[] ecost, CrossFlow ef) {
        //int tcost = Utils.ArraySum(flow);
        float[] costs = new float[2];
        costs[0] = ef.flowL2R + ef.flowL2D + ef.flowL2U
                + ef.flowR2L + ef.flowR2U + ef.flowR2D
                + ef.flowU2L + ef.flowD2R;
        costs[1] = ef.flowD2U + ef.flowD2L + ef.flowD2R
                + ef.flowU2D + ef.flowU2L + ef.flowU2R
                + ef.flowL2D + ef.flowR2U;

        //costs[0] = Math.max(0,tcost-costs[0]);
        //costs[1] = Math.max(0,tcost-costs[1]);
        costs[0] = -costs[0];
        costs[1] = -costs[1];

        float[] fcost = JudgeForwardCost(ecost, ef);

        costs[0] += fcost[0] * Constants.LAMBDA_2;
        costs[1] += fcost[1] * Constants.LAMBDA_2;

        return costs;
    }

    /**
     * flow中frmId到dstId的流量加上addf
     *
     * @param flow    - 当前流量
     * @param frmId   - 来源节点
     * @param dstId   - 目的节点
     * @param traffic - 交通结构图
     * @param addf    - 流量增加量
     */
    private static void AddMapFlow(Map<String, float[]> flow, String frmId, String dstId,
                                   TrafficGraph traffic, float addf) {
        TrafficCrossroad cross = traffic.getCrosses().get(dstId);

        if (cross != null) {
            String[] nns = cross.getNeighbors();

            for (int i = 0; i < nns.length; i++) {
                if (nns[i].compareTo(frmId) == 0) {
                    flow.get(dstId)[i] += addf;
                }
            }
        }
    }

    /**
     * 根据概率计算三个方向的流量
     *
     * @param flow      - 流入量
     * @param turnProba - 转弯概率
     * @return 三个方向的流量
     */
    public static float[] CalcTurnFlow(float flow, float[] turnProba) {
        float[] turn = Utils.ArrayScale(turnProba, flow);
        for (int i = 0; i < turn.length; i++) {
            turn[i] = Math.min(Constants.MAX_THROUGH_FLOW[i], turn[i]);
        }
        return turn;
    }

    /**
     * 根据当前流量计算各个节点的流出量
     *
     * @param traffic   - 交通结构图
     * @param currentFlow     - 当前流量
     * @param turnProb - 转弯概率
     * @return 各个节点的流出量
     */
    public static Map<String, float[]> CalcOutFlow(
            TrafficGraph traffic, Map<String, float[]> currentFlow, float[] turnProb) {

        Map<String, float[]> ret = new HashMap<String, float[]>();
        for (String cid : traffic.getCrosses().keySet()) {
            ret.put(cid, new float[4]);
        }

        for (Map.Entry<String, float[]> entry : currentFlow.entrySet()) {
            String cid = entry.getKey();
            float[] f = entry.getValue();
            String[] nns = traffic.getCrosses().get(cid).getNeighbors();

            for (int i = 0; i < f.length; i++) {
                String frm = cid;
                float[] tf = CalcTurnFlow(f[i], turnProb);

                for (int j = 0; j < tf.length; j++) {
                    int idst = (i + j) % 4;
                    String dst = nns[idst];

                    AddMapFlow(ret, frm, dst, traffic, tf[j]);
                }
                f[i] -= Utils.ArraySum(tf);
            }
        }

        return ret;
    }

    /**
     * 让流量自由流动，计算下个时间段的各个节点的流量
     *
     * @param traffic     交通图
     * @param currentFlow 当前流量
     * @return 下个时间段的流量
     */
    private static Map<String, float[]> Forward(TrafficGraph traffic,
                                               Map<String, float[]> currentFlow) {
        Map<String, float[]> ret = CopyFlow(currentFlow);
        Map<String, float[]> outFlow = CalcOutFlow(traffic, ret, Constants.TURN_PROB);

        FlowAdd(ret, outFlow);
        return ret;
    }

    /**
     * 将cost回馈
     *
     * @param traffic     - 交通结构图
     * @param currentCost - 当前的cost
     * @return 回馈的cost
     */
    public static Map<String, float[]> Backward(TrafficGraph traffic,
                                                Map<String, float[]> currentCost) {
        return CalcOutFlow(traffic, currentCost, Constants.TURN_PROB);
    }

    private static void FlowAdd(Map<String, float[]> dst, Map<String, float[]> src) {
        FlowAdd(dst, src, 1.0f);
    }

    /**
     * 将src的流量加入dst
     *
     * @param dst   - 目标流量
     * @param src   - 源流量
     * @param scale - 乘因子
     */
    private static void FlowAdd(Map<String, float[]> dst, Map<String, float[]> src, float scale) {//dst = dst+src
        for (Map.Entry<String, float[]> entry : src.entrySet()) {
            String cid = entry.getKey();
            float[] flow = entry.getValue();

            if (dst.containsKey(cid)) {
                Utils.ArrayAdd(dst.get(cid), flow, scale);
            } else {
                dst.put(cid, flow.clone());
            }
        }
    }

    /**
     * 将流量放大或缩小
     *
     * @param dst   - 目标
     * @param scale - 乘因子
     */
    private static void FlowScale(Map<String, float[]> dst, float scale) {
        for (Map.Entry<String, float[]> entry : dst.entrySet()) {
            entry.setValue(Utils.ArrayScale(entry.getValue(), scale));
        }
    }

    /**
     * 通过将流量前馈反馈算出每个节点四条边的权重，边的权重越大代表了将来可能会有大流量，
     * 所以越不能往这个方向走。
     *
     * @param traffic     交通图结构
     * @param currentTime 当前时间
     * @param interval    前馈时间个数
     * @return 每个节点对应4条边的权重
     */
    private static Map<String, float[]> FlowPropagate(TrafficGraph traffic, int currentTime,
                                                     int interval) {

        Map<String, float[]> currentFlow = traffic.getCurrentFlow();

        // 各个节点的流入量
        List<Map<String, float[]>> fflows = new ArrayList<Map<String, float[]>>();
        for (int i = 1; (i < interval) && ((currentTime + i) < Constants.MAX_TIME); i++) {
            //前馈
            Map<String, float[]> nextFlow = Forward(traffic, currentFlow);

            //加上每个路口突然出现的流量
            traffic.flowAdd(nextFlow, currentTime + i);
            fflows.add(nextFlow);
            currentFlow = nextFlow;
        }

        // 各个节点的流出量
        Map<String, float[]> cost = new HashMap<String, float[]>();
        int revInd = fflows.size() - 1;
        for (int i = 1; (i < interval) && ((currentTime + i) < Constants.MAX_TIME); i++) {
            FlowAdd(cost, fflows.get(revInd));

            Map<String, float[]> tcost = Backward(traffic, cost);
            FlowAdd(cost, tcost);
            revInd--;
        }

        return cost;
    }

    /**
     * 检查公平原则
     *
     * @param cid     - 节点id
     * @param time    - 当前时间
     * @param set     - 当前设定
     * @param traffic - 交通结构图
     * @return 是否违反公平原则
     */
    public static boolean IsMaxInterval(String cid, int time, int set, TrafficGraph traffic) {
        int[] history = traffic.getCrosses().get(cid).LightSettingHistory;

        if (time <= Constants.MAX_LIGHT_INTERVAL) {
            return false;
        }

        for (int i = time - 1; i >= time - Constants.MAX_LIGHT_INTERVAL; i--) {
            if (history[i] != set) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据cost确定红绿灯的设定
     *
     * @param cost         - 当前penalty
     * @param estimateCost - 根据前馈和反馈计算出的边的权重
     * @param setting      - 各个节点的设定,output
     * @param time         - 当前时间
     * @param traffic      -交通结构图
     * @return 总cost
     */
    public static float Judge(
            Map<String, float[]> cost, Map<String, float[]> estimateCost,
            Map<String, Integer> setting, int time, TrafficGraph traffic) {

        float tcost = 0.0f;

        for (Map.Entry<String, float[]> entry : cost.entrySet()) {
            String cid = entry.getKey();
            float[] flow = entry.getValue();

            int[] flowInt = Utils.ArrayFloat2Int(flow);
            CrossFlow cf = calCrossFlow(flowInt);

            float[] costs = JudgeCost(flowInt, estimateCost.get(cid), cf);

            int csetting = -1;

            if (costs[0] > costs[1]) {
                csetting = 1;
            } else {
                csetting = 0;
            }

            if (IsMaxInterval(cid, time, csetting, traffic)) {
                csetting = 1 - csetting;
            }
            setting.put(cid, csetting);
            tcost += costs[csetting];
        }
        //SimpleLog.info("" + setting.get("tl32"));
        return tcost;
    }

    /**
     * @param traffic 交通流量
     * @param time    时间
     * @return 概率
     */
    public static float SolveSingle(TrafficGraph traffic, int time) {

        // 估计交通图中每条边的权值
        Map<String, float[]> estimateCost = FlowPropagate(traffic, time,
                Constants.ESTIMATE_INTERVAL);

        // 将权值缩小一定比例
        FlowScale(estimateCost, Constants.LAMBDA_1);

        // 当前流量
        Map<String, float[]> cost = traffic.getCurrentFlow();

        Map<String, Integer> setting = new HashMap<String, Integer>();

        // 根据权值和流量计算红绿灯状态
        float tcost = Judge(cost, estimateCost, setting, time, traffic);

        traffic.setLight(setting, time);

        traffic.saveCurrentFlow();

        return tcost;
    }

    /**
     * @param traffic 交通灯的图
     * @param flow 新增流量
     * @param time 时间段
     * @return 概率
     */
    public static float Solve(TrafficGraph traffic, Map<String, int[]> flow, int time) {
        traffic.setCurrentFlow(flow);
        return SolveSingle(traffic, time);
    }
}
