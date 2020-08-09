/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Sort.java - an interface of sort method. Please implement this interface.
 */
package algorithms;

import java.util.ArrayList;
/**
 * 排序
 */
public interface Sort {
	/**
	 * @param popVar 种群
	 * @param popFit 适应度值
	 */
	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit);
}
