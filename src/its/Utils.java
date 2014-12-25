package its;

public class Utils 
{
	public static void ArrayAdd(float[] a, int[] b)
	{
		for(int i=0;i<a.length;i++)
		{
			a[i] += b[i];
		}
	}
	
	public static void ArrayAdd(float[] a, float[] b)
	{
		for(int i=0;i<a.length;i++)
		{
			a[i] += b[i];
		}		
	}
	
	public static void ArrayAdd(float[] a, float[] b, float scale)
	{
		for(int i=0;i<a.length;i++)
		{
			a[i] += b[i]*scale;
		}		
	}
	
	public static float[] ArrayScale(float[] a, float b)
	{
		float[] ret = new float[a.length];
		for(int i=0;i<a.length;i++)
		{
			ret[i] = a[i] * b;
		}
		
		return ret;
	}
	
	public static float ArraySum(float[] a)
	{
		float sum = 0.0f;
		for(float aa : a)
		{
			sum+=aa;
		}
		return sum;
	}
	
	public static long ArraySum(long[] a)
	{
		long sum = 0;
		for(long aa : a)
		{
			sum+=aa;
		}
		return sum;		
	}
	
	public static int ArraySum(int[] a)
	{
		int sum = 0;
		for(int aa : a)
		{
			sum+=aa;
		}
		return sum;		
	}
	
	public static long[] ArrayFloat2Long(float[] a)
	{
		long[] ret = new long[a.length];
		for(int i=0;i<a.length;i++)
		{
			ret[i] = (long) a[i];
		}
		
		return ret;
	}
	
	public static int[] ArrayFloat2Int(float[] a)
	{
		int[] ret = new int[a.length];
		for(int i=0;i<a.length;i++)
		{
			ret[i] = (int) a[i];
		}
		
		return ret;
	}
}
