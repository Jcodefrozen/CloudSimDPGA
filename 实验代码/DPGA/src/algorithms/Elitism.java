/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Elitism.java - an interface of elitism operator, please implement this interface.
 */
package algorithms;
/**
 * An abstraction of elitism.
 */
public abstract class Elitism {
	/** 将一小部分染色体传给下一代*/
	protected int elitSize = -1;

	/** 将百分之一的染色体传给下一代*/
	protected double elitPercent = -1;
	
	/** 优化方向，0表示最小化，1表示最大化 */
	protected int optimization;
	
	/** 构造函数
	 * @param elitSize 将会传给下一代的种群数量
	 * @param optimization 优化的方向
	 * */
	public Elitism(int elitSize, int optimization){
		this.elitSize = elitSize;
		this.optimization = optimization;
	}
	
	
	/**  constructor 
	 * @param elitPercent 将会传给下一代的种群比例
	 * @param optimization 优化目标
	 * */
	public Elitism(double elitPercent, int optimization){
		this.elitPercent = elitPercent;
		this.optimization = optimization;
	}
	
	
	/** 
	 * 根据精英的大小或精英的百分比将一些染色体传给下一代。
	 */
	public abstract void carryover(Chromosome[] popVar, Chromosome[] newPop);
	
	
	/**
	 * @return 将传递到下一代的染色体数目，如果未设置此值，则返回-1。
	 */
	public int getSize(){
		return elitSize;
	}

	/**
	 * @return 将被带到下一代的染色体的百分比，如果这个值没有被设置，返回-1。
	 */
	public double getPercent(){
		return elitPercent;
	}
}
