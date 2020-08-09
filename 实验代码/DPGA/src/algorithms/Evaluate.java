/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Evaluate.java - Interface of a Evaluation method.
 */
package algorithms;

import java.util.ArrayList;

/**
 * Defines the interface of evaluation
 * 
 * @author Boxiong Tan (Maximus Tann) 
 * @since Genetic Algorithm framework 1.0
 */
public interface Evaluate {
	/**
	 * @param popVar 带有一个染色体子类型的变量的种群
	 * @param popFit 适应度值
	 * @throws Exception 
	 */
	public void evaluate(Chromosome [] popVar,ArrayList<double[]> popFit);
}
