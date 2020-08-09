/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * SimulatedBinaryCrossover.java - A real value crossover method
 */
package commonOperators;

import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;
import commonRepresentation.RealValueChromosome;

/**
 * 一种实值变异方法
 */

public class SimulatedBinaryCrossover implements TwoParentsCrossover {
	/** 将distributionIndex设置为一个固定的数字1 */
	private double distributionIndex = 1;

	/**
	 * SBX steps：
	 *	对于每个变量，如果一个随机数小于crossoverRate，那么我们应用交叉
	 *  在0和1之间创建一个随机数u
	 *  使用多项式概率分布来计算
	 *   child1 = 0.5 * ((father + mother) - beta * abs(father - mother))
	 *   apply crossover, child2 = 0.5 * ((father + mother) + beta * abs(father - mother))
	 * @param father
	 * @param mother
	 * @param crossoverRate
	 */
	@Override
	public Chromosome[] update(Chromosome father, Chromosome mother, double crossoverRate) {
		int chromoSize = father.size();
		RealValueChromosome[] children = new RealValueChromosome[2];
		double beta;

		RealValueChromosome child1 = new RealValueChromosome(chromoSize);
		RealValueChromosome child2 = new RealValueChromosome(chromoSize);
		for(int i = 0; i < chromoSize; i++){
			double randomNum = StdRandom.uniform();
			if(randomNum < crossoverRate){
				double u = StdRandom.uniform();
				if(u <= 0.5){
					beta = Math.pow((2 * u), (1 / (distributionIndex + 1)));
				} else {
					beta = Math.pow((1 / (2 * (1 - u))), (1 / (distributionIndex + 1)));
				}
				double process1 = (((RealValueChromosome) father).individual[i] +
								    ((RealValueChromosome) mother).individual[i]);

				double process2 =  (((RealValueChromosome) father).individual[i] -
								    ((RealValueChromosome) mother).individual[i]);

				if(process2 < 0) process2 = - process2;
				child1.individual[i] = 0.5 * (process1 + beta * process2);
				child2.individual[i] = 0.5 * (process1 - beta * process2);
			} 
		else {
				child1.individual[i] = ((RealValueChromosome) father).individual[i];
				child2.individual[i] = ((RealValueChromosome) mother).individual[i];
			}
		}
		children[0] = child1;
		children[1] = child2;
		return children;
	}

}
