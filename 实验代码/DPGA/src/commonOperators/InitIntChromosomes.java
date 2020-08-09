/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitIntChromosomes.java - A int type initialization implementation
 */
package commonOperators;

import algorithms.*;
import commonRepresentation.IntValueChromosome;

/**
 * 初始化Int GA的种群
 *
 */
public class InitIntChromosomes implements InitPop{
    /**
     * 生成染色体
     *
	 * @param popSize 		种群大小
	 * @param maxVar 		染色体大小
     * @param lbound 染色体某一变量的下界
     * @param ubound 染色体某一变量的上界
     * @return 种群变量大小
     */	
	@Override
	public IntValueChromosome[] init(
						int popSize, 
						int maxVar, 
						double lbound, 
						double ubound
						) {
		IntValueChromosome[] popVar = new IntValueChromosome[popSize];
		
		// initialize population
		for(int i = 0; i < popSize; i++){
			popVar[i] = new IntValueChromosome(maxVar);
			for(int j = 0; j < maxVar; j++){
				popVar[i].individual[j] = StdRandom.uniform((int) lbound, (int) ubound);
			}
		}
		return popVar;
	}
	
}
