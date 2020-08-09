/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitBinaryChromosome.java - A binary type initialization implementation
 */
package commonOperators;

import commonRepresentation.IntValueChromosome;

/**
 * 二进制遗传算法的种群初始化
 */
public class InitBinaryChromosome extends InitIntChromosomes {

	/**
	 * 二进制染色体是整型值染色体的一个子类型，因此，扩展了整型值init方法
	 * @param popSize 		种群大小
	 * @param maxVar 		染色体大小
	 * @param lbound 		override by 0
	 * @param ubound			override by 1
	 * @return
	 */
	@Override
	public IntValueChromosome[] init(
								int popSize, 
								int maxVar, 
								double lbound, 
								double ubound
								){
		lbound = 0;
		ubound = 2;
		return super.init(popSize, maxVar, lbound, ubound);
	}
}
