/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * TwoParentsCrossover.java - an interface of crossover operator, please implement this interface.
 */
package algorithms;

/**
 * 交叉操作
 */
public interface TwoParentsCrossover extends Crossover {
    /**
     * 
     * @param mother 选择的染色体
     * @param father
     * @param crossoverRate 交叉概率
     * @return 子代染色体的概率
     */	
	public Chromosome[] update(
						Chromosome father,
						Chromosome mother,
						double crossoverRate
						);
}
