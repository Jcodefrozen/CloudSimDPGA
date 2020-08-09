/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * GeneticAlgorithm.java - An abstract of Genetic algorithm. It does not contain the
 * 							implementation of the algorithm
 */

package algorithms;

import java.util.ArrayList;

import algorithms.StdRandom;
import dataCollector.DataCollector;

public abstract class GeneticAlgorithm {
	/**用于初始化填充的InitPop对象*/
	protected InitPop initPop;

	/** 用于评估适应度的评估对象*/
	protected Evaluate evaluate;

	/** 选择操作*/
	protected Selection selection;

	/** 交叉操作 */
	protected Crossover crossover;

	/** 变异操作*/
	protected Mutation mutation;
	
	/** 精英策略 */
	protected Elitism elitism;
	
	/** 约束 */
	protected Constraint constraint;
	
	/** 距离方法 */
	protected Distance distance;

	/** 用于收集结果的DataCollector对象*/
	protected DataCollector collector;

	/**一个种群变量数组，数组的大小就是种群的大小*/
	 protected Chromosome[] popVar;

	 /** 一份列表[适应度值，排名] */
	 protected ArrayList<double[]> popFit;
	 
	 /** 排序方法 */
	 protected Sort sort;

	// Problem related parameter settings
	/** 如果optimization== 1，则算法使适应度值最大化，否则算法使适应度值最小化 */
	protected int optimization;

	/**
	 * 变异概率
	 */
	protected double mutationRate;

	/**
	 * 遗传到下一代的染色体的大小
	 */
	protected int elitSize;

	protected int tournamentSize;
	
	
	/** 交叉概率*/
	protected double crossoverRate;

	/** 种群大小 */
	protected int popSize;

	/** 最大迭代次数*/
	protected int maxGen;

	/** 染色体大小*/
	protected int maxVar;

	/** 染色体某一变量的下界 */
	protected double lbound;

	/** 染色体上一个变量的上边界*/
	protected double ubound;

    /**
     * 我们不使用构造函数，而是使用prepare()来初始化GA，包括赋值，这应该在子类中扩展和完成。
     */
	protected abstract void prepare();

    /**
     * 初始化随机生成器。
     */
	public void initializeRand(int seed){
		StdRandom.setSeed(seed);
	}

    /**
     * GA的实际过程，没有在这里实现这个过程。
     * @param seed the random seed
     */
	public abstract void run(int seed);
	
	/**
	 * 重复N次实验
	 * 
	 * @param seedStart 随机的种子从这个种子开始，每次增加1。
	 * @param nTimes 做n次实验
	 */
	public abstract void runNtimes(int seedStart, int nTimes);

}
