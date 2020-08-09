/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * PolyMutation.java - A real value mutation method
 */
package commonOperators;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;
import commonRepresentation.RealValueChromosome;


public class PolyMutation implements Mutation {
	private double lbound, ubound;
	private double perturbation;

	/**
	 * Constructor
	 * @param lbound 某一变量的下界
	 * @param ubound 某一变量的上界
	 */
	public PolyMutation(double lbound, double ubound, double perturbation){
		this.lbound = lbound;
		this.ubound = ubound;
		this.perturbation = perturbation;
	}
	/**
	 * 详细描述请查看上面的链接。
	 * @param popVar 种群
	 * @param mutationRate 变异概率
	 */
	@Override
	public void update(Chromosome individual, double mutationRate) {
		polynomialMutation((RealValueChromosome) individual, mutationRate);
	}

	/**
	 * @param individual
	 * @param mutationRate
	 */
	private void polynomialMutation(RealValueChromosome individual, double mutationRate){
		double L, R, u;
		u = StdRandom.uniform();
		for(int j = 0; j < individual.size(); j++) {
			if(StdRandom.uniform() <= mutationRate) {
				if(u < 0.5){
					L = Math.pow((2 * u), (1 / (1 + perturbation))) - 1.0;
					individual.individual[j] = individual.individual[j] + L * (individual.individual[j] - lbound);
				} else {
					R = 1 - Math.pow(Math.abs(2 * (1 - u)), (1 / (1 + perturbation)));
					individual.individual[j] = individual.individual[j] + R * (ubound - individual.individual[j]);
				}
			} 
		}
	}
}
