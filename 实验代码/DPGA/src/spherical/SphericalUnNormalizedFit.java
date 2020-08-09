/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * SphericalUnNormalizedFit.java an implementation of unNormalizedFit
 */
package spherical;

import algorithms.Chromosome;
import algorithms.UnNormalizedFit;
import commonRepresentation.RealValueChromosome;

public class SphericalUnNormalizedFit extends UnNormalizedFit{
	/**
	 * @param individual 需要评估的染色体
	 */
	public SphericalUnNormalizedFit(Chromosome individual){
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
			fit += ((RealValueChromosome) individual).individual[j] * 
				   ((RealValueChromosome) individual).individual[j];
		}
		// just initialize the ranking with 0, because this call() will be executed in thread, 
		// therefore it is hard to initialize ranking here.
		
		return new double[]{fit, 0};
	}
}
