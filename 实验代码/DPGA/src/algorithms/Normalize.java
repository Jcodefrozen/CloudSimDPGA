/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Normalize.java - An interface of normalization
 */
package algorithms;

import java.util.ArrayList;


public interface Normalize {
	
    /**
     * 将给定的适应度值标准化
     * 
     * @param fitness 适应度值数组
     * @return 一组规范化的适应度值
     */
	public ArrayList<double[]> doNorm(ArrayList<double[]> fitness);
}
