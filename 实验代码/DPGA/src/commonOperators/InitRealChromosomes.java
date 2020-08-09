/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * InitRealChromosomes.java - A double type initialization implementation
 */
package commonOperators;

import algorithms.*;
import commonRepresentation.RealValueChromosome;

/**
 * 初始化Double GA种群
 *
 */
public class InitRealChromosomes implements InitPop{
    /**
     *
	 * @param popSize 		种群大小
	 * @param maxVar 		染色体大小
	 * @param lbound 染色体某一变量的下界
	 * @param ubound 染色体某一变量的上界
	 * @return 种群变量
     */
	@Override
	public RealValueChromosome[] init(
						int popSize,
						int maxVar,
						double lbound,
						double ubound
						) {
		RealValueChromosome[] popVar = new RealValueChromosome[popSize];

		// initialize population
		for(int i = 0; i < popSize; i++){
			popVar[i] = new RealValueChromosome(maxVar);
			for(int j = 0; j < maxVar; j++){
				popVar[i].individual[j] = StdRandom.uniform(lbound, ubound);
			}
		}
		return popVar;
	}
}
