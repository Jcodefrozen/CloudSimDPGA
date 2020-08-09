/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Mutation.java - an interface of mutation operator, please implement this interface.
 */
package algorithms;

/**
 * 变异操作接口
 */
public interface Mutation {
    /**
     * 
     * @param individual 一条染色体
     * @param mutationRate 变异概率
     */	
	public void update(
			Chromosome individual,
			double mutationRate 
			);
	
}
