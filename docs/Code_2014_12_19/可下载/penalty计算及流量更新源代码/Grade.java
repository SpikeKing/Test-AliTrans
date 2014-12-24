package com.tmall.judge.grade;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Grade {

	//从Traffic_Light_Table.txt读取所有路口红绿灯位置关系信息
	static Map<String, String[]> trafficLightTable = new ConcurrentHashMap<String, String[]>();
	
	//所有路口某个小时的静态流量表和动态流量表(数组长度为120)
	static Map<String, Integer[]> staticFlowTable = new ConcurrentHashMap<String,Integer[]>();
	static Map<String, Integer[]> dynamicFlowTable = new ConcurrentHashMap<String,Integer[]>();
	
	//传出给选手参数:某个小时某个时段T(i)所有路口的车流量
	static Map<String, Integer> currentFlows = new ConcurrentHashMap<String,Integer>();
	
	//传出给选手参数:某个时段T(i)所有路口车辆的 转向概率:[左转,右转,直行]
	static Map<String, Double[]> turnRate = new ConcurrentHashMap<String,Double[]>();
	
	//传出给选手参数:某个时段T(i)所有路口车辆的 通过率:[左转,右转,直行]
	static Map<String, Integer[]> throughRate = new ConcurrentHashMap<String,Integer[]>();
	
	//选手传入参数：某个时段T(i)所有路口红 绿灯的状态:[左转,右转,直行] (红灯为0，绿等为1,缺失-1)
	static Map<String, Integer[]> currentStatus = new ConcurrentHashMap<String,Integer[]>();
	
	//某个小时内红绿灯的历史信息
	static Map<String, ArrayList<Integer[]>> statusHistory = new ConcurrentHashMap<String,ArrayList<Integer[]>>();
	
	//14个小时的penalty
			
	//所有taskId对应的penalty
	public static Map<String, Double[]> penaltyMap =  new ConcurrentHashMap<String, Double[]>();
	public static boolean inited = false;
	//初始化每个选手对应的penalty数组
	public static void initPenalty(String taskId) {
		Double[] penalty = {0.0,0.0,0.0,0.0,
                             0.0,0.0,0.0,0.0,
                             0.0,0.0,0.0,0.0,0.0,0.0};
		penaltyMap.put(taskId, penalty);
	}
	
	//从trafficLightTable读取红绿灯位置信息
	public static void setLightInfo(String taskId, Map<String, String[]> trafficLightMap){
		for (String aKey : trafficLightMap.keySet()) {
			String[] keyStrings = aKey.split("-");
			String taks_key = keyStrings[0] + "-" + keyStrings[1] + "-" + taskId;
			trafficLightTable.put(taks_key,trafficLightMap.get(aKey));
		}

	}
	
	// 第i个小时状态初始化
	public static void hourInit(String taskId, Map<String, Integer[]> trafficFlowMap,int i) throws NumberFormatException, IOException {
		
		for (String aKey : trafficFlowMap.keySet()) {
			String[] keyStrings = aKey.split("-");
			String taks_key = keyStrings[0] + "-" + keyStrings[1] + "-" + taskId;
			
			  Integer[] values = new Integer[120];
			  for (int i1 = 0; i1 < values.length; i1++) {
				 values[i1] = trafficFlowMap.get(aKey)[i*120 + i1];			
			}
			staticFlowTable.put(taks_key,values);
			dynamicFlowTable.put(taks_key,values.clone());
		}
		
		//初始化turnRate:[0.1,0.1,0.8]、throughRate:[2,2,16]、statusHistory
		for(String key : trafficLightTable.keySet()){

			if(!taskId.equals(key.split("-")[2])){
				continue;
			}
			//十字路口转向概率
			Double[] initTurnRate = {0.1,0.1,0.8};
			
			//丁字路口,无左转方向
			if (trafficLightTable.get(key)[0].equals("#")) {
				initTurnRate[1] += initTurnRate[0];
				initTurnRate[0] -= initTurnRate[0];				
			}else if (trafficLightTable.get(key)[1].equals("#")) {
				//丁字路口,无右转方向
				initTurnRate[0] += initTurnRate[1];
				initTurnRate[1] -= initTurnRate[1];
			}else if (trafficLightTable.get(key)[2].equals("#")) {
				//丁字路口,无直行方向
				initTurnRate[0] += initTurnRate[2]*0.5;
				initTurnRate[1] += initTurnRate[2]*0.5;
				initTurnRate[2] -= initTurnRate[2];
			}
			
			Integer[] initThroughRate = {2,2,16};
			
			ArrayList<Integer[]> initStatusHistory = new ArrayList<Integer[]>();
			for (int j = 0; j < 120; j++) {
				
				Integer[] aStatus = {0,0,0};
			if ("#".equals(trafficLightTable.get(key)[0])) {
				aStatus[0] = -1;
			}else if ("#".equals(trafficLightTable.get(key)[1])) {
				aStatus[1] = -1;
			}else if ("#".equals(trafficLightTable.get(key)[2])) {
				aStatus[2] = -1;
			}
			
				initStatusHistory.add(aStatus);
			}
			
			turnRate.put(key, initTurnRate);
			throughRate.put(key, initThroughRate);
			statusHistory.put(key, initStatusHistory);
		}
	}
	
	//根据最近一段时间红绿历史状态表计算转向概率[ALPHA,BETA,GAMMA],初赛为固定值不更新
	public static void updateTurnRate() {
		
		/**for (String key : statusHistory.keySet()) {
			Double[] rate = {0.1,0.1,0.8};
			turnRate.put(key, rate);
		}*/
	}
	
	//根据路口车流量计算车辆通过率[TH1,TH2,TH3],初赛通过率固定值为不更新
	public static void updateThroughRate() {
		/*
		for(String key : currentFlows.keySet()){
			Integer[] rate = {5,5,20};
			throughRate.put(key, rate);
		}*/
	}

	//选手传入参数：第i时刻currentStatus,更新到statusHistory
	public static void setCurrentStatus(Map<String, Integer[]> aStatus,int i) {
		
		//currentStatus = aStatus;
		
		for(String key :aStatus.keySet()){
			if (statusHistory.containsKey(key)) {
				for (int j = 0; j < 3; j++) {
					if ("#".equals(trafficLightTable.get(key)[j])) {
						//如果路口不通，强制将灯设置为-1
						statusHistory.get(key).get(i)[j] = -1;
					} else {
						//如果路口是通的，不允许选手将灯设置为-1
						statusHistory.get(key).get(i)[j] = Math.max(0, aStatus.get(key)[j]);
					}
				}
				
			}
		}
	}
	
	//根据statusHistory(i)、dynamicFlowTable(i)计算T(i)时刻某路口滞留车辆数
	public static int computeStay(String key, int i) {
		
		//左转右转直行实际能够通过的车辆数
		int leftThrough,rightThrough,straightThrough;
		leftThrough=rightThrough=straightThrough = 0;
		//绿灯时通过率才有效
		if (statusHistory.get(key).get(i)[0]==1) {
			leftThrough = throughRate.get(key)[0];
		}
		if (statusHistory.get(key).get(i)[1]==1) {
			rightThrough = throughRate.get(key)[1];
		}
		if (statusHistory.get(key).get(i)[2]==1) {
			straightThrough = throughRate.get(key)[2];
		}
		
		//dynamicFlow(i)
		int iFlow = dynamicFlowTable.get(key)[i];
		//左转右转直行的滞留车辆
		int leftStay,rightStay,straightStay;
		leftStay = rightStay = straightStay = 0;
		
		double leftRate = turnRate.get(key)[0];
		double rihgtRate = turnRate.get(key)[1];
		double straightRate = turnRate.get(key)[2];

		//上取整可能会导致流量增大一点
		leftStay = Math.max(0, (int)Math.ceil(iFlow*leftRate) - leftThrough);
		rightStay = Math.max(0, (int)Math.ceil(iFlow*rihgtRate) - rightThrough);
		straightStay = Math.max(0,(int)Math.ceil(iFlow*straightRate)-straightThrough);

		//LOGGER.error("key="+key+",i="+i+",leftStay="+leftStay+",rightStay="+rightStay+",straightStay="+straightStay);
		//返回T(i)时刻滞留车辆数
		//System.out.println("allStay===" +(leftStay + rightStay + straightStay));
		return (leftStay + rightStay + straightStay);
		
	}
	
	/*根据红绿灯位置信息表、T(i-1)时段的：车流量、所有红绿灯状态、车辆转向概率、车辆通过率 
     *计算T(i)时刻车流量
     *dynamicFlowTable(i)=LAMADA*staticFlowTable(i) + G(trafficLightTable,statusHistory(i-1),dynamicFlowTable(i-1),turnRate(i-1),throughRate(i-1))
     */
	public static void updateDynamicFlowTable(String taskId, int i) {
		
		for(String key :dynamicFlowTable.keySet()){
    		
			String tId = key.split("-")[2];
    		
			if (tId.equals(taskId)) {
				if (i==0) {
		    		dynamicFlowTable.get(key)[i] =  staticFlowTable.get(key)[i];					
				} else {
					
					//观察值系数LAMADA
					double LAMADA = 0.5;
					//更新flow,加上系数LAMADA*staticFlow(i)
					dynamicFlowTable.get(key)[i] = (int)(Math.floor(LAMADA*staticFlowTable.get(key)[i]));
					
					//结合statusHistory(i-1)加上 dynamicFlow(i-1)的 左转滞留+右转滞留+直行滞留						
					int allStay = 0;
					//优化代码用函数实现
					allStay = computeStay(key, i-1);											
					//更新，加上滞留车辆
					dynamicFlowTable.get(key)[i] += allStay;
										
					//加上FromID从其他路口流入的车辆
					int flowIn1,flowIn2,flowIn3;
					flowIn1 = flowIn2 = flowIn3 = 0;
					
					//反向路径ID
					String[] keyStrings = key.split("-");
					String antiKey = keyStrings[1] + "-" + keyStrings[0] + "-" + taskId;
					//反向路径存在于trafficLightTable中才有意义
					if (trafficLightTable.containsKey(antiKey)) {
						//流入车辆的来源ID
						String antiLeftID = trafficLightTable.get(antiKey)[0];
						String antiRightID = trafficLightTable.get(antiKey)[1];
						String antiStraightID = trafficLightTable.get(antiKey)[2];
							
						String antiLeftKey = keyStrings[1]  + "-" + antiLeftID + "-" + keyStrings[2];
						String antiRightKey = keyStrings[1] + "-" + antiRightID + "-" + keyStrings[2];
						String antiStraightKey = keyStrings[1] + "-" + antiStraightID + "-" + keyStrings[2];
							

							
						//从 antiLeftID 右转流入
						if (!(trafficLightTable.get(antiLeftKey)==null)&&(statusHistory.get(antiLeftKey).get(i-1)[1]==1)) {
							flowIn1 = Math.min(throughRate.get(antiLeftKey)[1], (int)Math.ceil(dynamicFlowTable.get(antiLeftKey)[i-1]*turnRate.get(antiLeftKey)[1]));
						}
						//从 antiRightID 左转流入
						if (!(trafficLightTable.get(antiRightKey)==null)&&(statusHistory.get(antiRightKey).get(i-1)[0]==1)) {
							flowIn2 = Math.min(throughRate.get(antiRightKey)[0], (int)Math.ceil(dynamicFlowTable.get(antiRightKey)[i-1]*turnRate.get(antiRightKey)[0]));
						}
						//从 antiStraightKey 直行流入
						if(!(trafficLightTable.get(antiStraightKey)==null)&&(statusHistory.get(antiStraightKey).get(i-1)[2]==1)){
							flowIn3 = Math.min(throughRate.get(antiStraightKey)[2], (int)Math.ceil(dynamicFlowTable.get(antiStraightKey)[i-1]*turnRate.get(antiStraightKey)[2]));
						}
					}					

					//更新，加上流入车辆
					dynamicFlowTable.get(key)[i] += flowIn1 + flowIn2 + flowIn3;
					//System.out.println("  " + key +"-" + i + " flow:" +dynamicFlowTable.get(key)[i] );
						
				}
			}
		}
			
	}

	//用 dynamicFlowTable 设置 currentFlow 传出给选手
	public static Map<String, Integer> getCurrentFlow(String taskId, int i){

			for(String key : dynamicFlowTable.keySet()){
				if (taskId.equals(key.split("-")[2])) {
					currentFlows.put(key, dynamicFlowTable.get(key)[i]);
				}
			}
			return currentFlows;
	}	

	//根据currentStatus(i)更新第k个小时penalty
	public static void updatePenalty(String taskId,int i,int k){
		
		//每个路口T(i)时刻penalty: 左转滞留+右转滞留+直行滞留;违反交通规则扣分;违反公平性原则扣分
		for(String key : dynamicFlowTable.keySet()){
			
			String[] lights = key.split("-");
			if (taskId.equals(lights[2])) {
				//更新，车辆滞留部分
				penaltyMap.get(taskId)[k] += computeStay(key, i);
				//更新，加上红绿灯违反交通规则的惩罚 a:直行垂直直行惩罚 b:直行垂直左转惩罚
				double a,b;
				a=b=0.0;			
				//交通违规的惩罚倍数
				double zeta =0.5;
				
				String leftKey = lights[0] + "-" + trafficLightTable.get(key)[0] + "-" + lights[2];
				String rightKey = lights[0] + "-" + trafficLightTable.get(key)[1] + "-" + lights[2];
				
				//垂直方向不能同时直行																				
				if (statusHistory.get(key).get(i)[2]==1 &&
					((statusHistory.containsKey(leftKey) && statusHistory.get(leftKey).get(i)[2]==1) 
					|| (statusHistory.containsKey(rightKey) && statusHistory.get(rightKey).get(i)[2]==1))) {
						
					a += zeta*dynamicFlowTable.get(key)[i];
						
					if (dynamicFlowTable.containsKey(leftKey)) {
							a += zeta*dynamicFlowTable.get(leftKey)[i];
					}
						
					if (dynamicFlowTable.containsKey(rightKey)) {
							a += zeta*dynamicFlowTable.get(rightKey)[i];
					}
						
				}
				
				//直行时垂直方向右侧不能左转
				if (statusHistory.get(key).get(i)[2]==1 && statusHistory.containsKey(rightKey) && statusHistory.get(rightKey).get(i)[0]==1) {

						b += zeta*(dynamicFlowTable.get(rightKey)[i] + dynamicFlowTable.get(key)[i]);
				}
				
				//违规扣分
				penaltyMap.get(taskId)[k] += 0.5*a + b;

				//更新，加上违反公平原则扣分 v*sqrt(r-4)
				if (i>3) {
					for (int j = 0; j < 3; j++) {
						if (statusHistory.get(key).get(i)[j]==0) {
							int waitTime = 1;
							int waitStart = i;
							//修改bug
							while ((waitStart>0) && statusHistory.get(key).get(waitStart-1)[j]==0) {
								waitTime += 1;
								waitStart -=1;
							}
							penaltyMap.get(taskId)[k] += (int)Math.ceil(dynamicFlowTable.get(key)[i]*Math.sqrt(Math.max(waitTime-4, 0)));
						}
					}
				}
				
			}
																													
		}
		//System.out.println(k + "th hour" + i + "th Penalty======" + penaltyMap.get(taskId)[k]);
		
	}
	
	
	//清除某个用户的缓存数据
	public static void cleanCache(String taskId) {
		List<String> list = Lists.newArrayList(trafficLightTable.keySet());
		for(String key : list){
			String[] keyStrings = key.split("-");
			if (keyStrings[2].equals(taskId)) {
				trafficLightTable.remove(key);
				staticFlowTable.remove(key);
				dynamicFlowTable.remove(key);
				turnRate.remove(key);
				throughRate.remove(key);
				statusHistory.remove(key);
			}
		}
		penaltyMap.remove(taskId);
	}
	
	//输出选手最终得分
	public static int getGrade(String taskId) {
		
		if (penaltyMap.containsKey(taskId)) {
			int score = 0;
			for (int i = 0; i < penaltyMap.get(taskId).length; i++) {
				score += penaltyMap.get(taskId)[i];	
		}			

            //清缓存数据
			cleanCache(taskId);
            System.out.println("returnSumScore====" + score);
			return score;
		} else {
			//出异常的选手得分为-1
			return -1;
		}			
	}
	


}




