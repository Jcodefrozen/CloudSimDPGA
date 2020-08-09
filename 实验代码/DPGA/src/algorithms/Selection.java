/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Selection.java - an interface of selection operator. Please implement this interface.
 */

package algorithms;

import java.util.ArrayList;

/**
 * 选择操作
 */
public interface Selection {
    /**
     * 选择过程
     * 
     * @param popVar 种群
     * @param popFit 适应度值
     * @return 返回所选染色体的索引。
     */	
	public int selected (
			Chromosome [] popVar,
			ArrayList<double[]> popFit
			);
}
