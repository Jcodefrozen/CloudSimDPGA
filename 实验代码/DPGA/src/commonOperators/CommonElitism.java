/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CommonElitism.java - an implementation of elitism.
 */
package commonOperators;


import algorithms.Chromosome;
import algorithms.Elitism;
/**
 * 精英策略
 *
 */
public class CommonElitism extends Elitism{
	
	/**
	 * 
	 * @param elitSize 复制到下一代的染色体的大小
	 * @param optimization 优化目标
	 */
	public CommonElitism(int elitSize, int optimization) {
		super(elitSize, optimization);
	}
	
	/**
	 * 
	 * @param elitPercent 遗传给下一代的染色体的百分比
	 * @param optimization 优化方向
	 */
	public CommonElitism(double elitPercent, int optimization) {
		super(elitPercent, optimization);
	}
	/**
	 * 将一小部分染色体复制到下一代
	 */
	@Override
	public void carryover(Chromosome[] popVar, Chromosome[] newPop){
		int popSize = popVar.length;
		if(elitPercent != -1){
			elitSize = (int) (elitPercent * popSize);
		}
		if(optimization == 0){
			for(int i = 0; i < elitSize; i++){
				newPop[i] = popVar[i].clone();
			}
		} else {
			for(int i = 0; i < elitSize; i++){
				newPop[i] = popVar[popSize - 1 - i].clone();
			}
		}
	}


}
