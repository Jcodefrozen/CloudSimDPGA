/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * DataCollector.java - An Interface of Data collector.
 */
package dataCollector;

import java.util.ArrayList;

/**
 * data collector
 * 此默认数据收集器只包含时间收集器
 * 您想要收集的所有实际数据必须在您的扩展中定义
 *
 */
public abstract class DataCollector {
	protected ArrayList<Double> timeData;
	protected long start, end;


	// initialize timer field
	public DataCollector(){
		timeData= new ArrayList<Double>();
	}


    /**
     * 收集结果
     * @param result 可以是二维数组还是一维数组
     */
	public abstract void collect(Object result);

	/**
	 * 个体
	 */
	public abstract void collectSet(Object set);


	/**
	 * 如果标志为0，启动计时器否则停止计时器。
	 *
	 * 把时间换算成秒，保留两位小数
	 */
	public void collectTime(int flag){
		if(flag == 0) start = System.nanoTime();
		else {
			end = System.nanoTime();
			timeData.add(new Double(Math.floor((end - start) / 10000000.0)) / 100.0);
		}
	}

	/**
	 * 打印一次运行的平均时间间隔。
	 */
	public void printMeanTime(){
		int size = timeData.size();
		double sum = 0;
		for(int i = 0; i < size; i++){
			sum += timeData.get(i);
		}
		System.out.println("time is : " + Math.floor(sum / size * 100) / 100.0);
	}

	// 返回运行的平均时间
	public double meanTime(){
		int size = timeData.size();
		double sum = 0;
		for(int i = 0; i < size; i++){
			sum += timeData.get(i);
		}
		return Math.floor(sum / size * 100) / 100.0;
	}

	// 返回运行的sd时间
	public double sdTime(){
		int size = timeData.size();
		double aveTime = meanTime();
		double sdSum = 0.0;
		for(int i = 0; i < size; i++){
		    sdSum += timeData.get(i) * timeData.get(i);
		}

		return Math.sqrt(sdSum / timeData.size() - aveTime * aveTime);
	}

	/**
	 *
	 * @return 时间数据一个双精度的列表
	 */
	public ArrayList<Double> getTime(){
		return timeData;
	}
}
