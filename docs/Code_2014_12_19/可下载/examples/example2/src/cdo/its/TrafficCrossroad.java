package cdo.its;

import java.util.ArrayList;
import java.util.List;

/***
 * 
 * 交通十字路口
 *
 */
public class TrafficCrossroad 
{
	public String id; //路口id
	public String[] neighbours; //相邻的路口,顺序为左上右下
	
	public int[] currentFlow; //当前流量
	public int currentTime; //当前时间
	public List<int[] > flowHistory; //历史流量
	public int[] lightSettingHistory; //历史设定状态
	public int[][] flowAdd; //每个时间段突然出现的流量（预估）
	
	public void setNeightbours(String left, String up, String right, String down)
	{
		this.neighbours = new String[4];
		neighbours[0] = left;
		neighbours[1] = up;
		neighbours[2] = right;
		neighbours[3] = down;
	}
	
	public TrafficCrossroad(String id)
	{
		this.id = id;
		
		this.flowAdd = new int[Constants.MAX_TIME][];
		for(int i=0;i<flowAdd.length;i++)
		{
			flowAdd[i] = new int[4];
		}
		
		this.lightSettingHistory = new int[Constants.MAX_TIME+1];
		flowHistory = new ArrayList<int[]>();
	}
	
	public void setLight(int setting, int time)
	{
		this.lightSettingHistory[time] = setting;
	}
}
