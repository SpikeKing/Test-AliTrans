package cdo.its;

/***
 * 
 * 路口的流量通行情况,L代表左,R代表右,U代表上,D代表下
 * flowL2R代表从左边流向上边的流量，其他以此类推。
 *
 */
public class CrossFlow
{
	public int flowL2R;
	public int flowL2U;
	public int flowL2D;
	
	public int flowU2D;
	public int flowU2R;
	public int flowU2L;
	
	public int flowR2L;
	public int flowR2D;
	public int flowR2U;		
	
	public int flowD2U;
	public int flowD2L;
	public int flowD2R;
}
