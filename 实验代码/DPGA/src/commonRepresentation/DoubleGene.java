/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * DoubleGene.java - A double type gene implementation
 */
package commonRepresentation;

import algorithms.Gene;
public class DoubleGene implements Gene {
	/** actual content */
	public double[] gene;

	/** Constructor 
	 * @param size 基因大小
	 */	
	public DoubleGene(int size){
		gene = new double[size];
	}

	@Override
	/* 获取种群大小 */
	public int size() {
		return gene.length;
	}
}
