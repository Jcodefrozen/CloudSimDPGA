/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntReverseSequenceMutation.java - An implementation of int mutation
 */
package commonOperators;

import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;
import commonRepresentation.IntValueChromosome;

public class IntReverseSequenceMutation implements Mutation {

	@Override
	/**
	 * Steps:
	 * 	 1.检查[0,1]中的随机数是否小于mutationRate。
	 * 	 2. 如果是，做逆向序列突变。
	 */
	public void update(Chromosome individual, double mutationRate) {

		if(StdRandom.uniform() <= mutationRate) {
			reverseSequence((IntValueChromosome) individual);
		}
	}

	/**
	 * 步骤:
	 * 	1. 随机选择两个点:startPoint和endPoint, startPoint < endPoint
	 * 	2. 把这两个点的顺序序列一下。
	 *
	 * @param chromosome 个体
	 */
	private void reverseSequence(IntValueChromosome chromosome){
		int startPoint, endPoint;
		int chromoSize = chromosome.size();
		endPoint = StdRandom.uniform(0, chromoSize);
		startPoint = StdRandom.uniform(0, endPoint);

		int[] temp = new int[endPoint - startPoint];

		for(int i = startPoint, j = 0; i < endPoint; i++, j++)
			temp[j] = chromosome.individual[i];

		for(int i = startPoint, j = endPoint - startPoint - 1; i < endPoint; i++, j--){
			chromosome.individual[i] = temp[j];
		}
	}

}
