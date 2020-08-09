/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RosenbrockUnNormalizedFit.java an implementation of unNormalizedFit
 */
package Rosenbrock;

import algorithms.Chromosome;
import algorithms.UnNormalizedFit;
import commonRepresentation.RealValueChromosome;

public class RosenbrockUnNormalizedFit extends UnNormalizedFit{
	/**
	 * @param individual
	 */
	public RosenbrockUnNormalizedFit(Chromosome individual){
		super(individual);
	}
	
	
	@Override
	/**
	 * 需要实现call()函数，因为unNormalizedFit实现了可调用接口。
	 */
	public Object call() throws Exception {
		int maxVar = individual.size();
		
		double fit = 0.0;
		for(int j = 0; j < maxVar - 1; j++){
			double firstTerm, secondTerm;
			firstTerm = ((RealValueChromosome) individual).individual[j + 1] - 
					Math.pow(((RealValueChromosome) individual).individual[j], 2);
			secondTerm = Math.pow(((RealValueChromosome) individual).individual[j] - 1, 2);
			fit += 100 * (firstTerm * firstTerm) + secondTerm;
		}
		return new double[]{fit, 0};
	}
}
