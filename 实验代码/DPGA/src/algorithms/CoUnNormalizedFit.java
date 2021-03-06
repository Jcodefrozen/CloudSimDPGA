/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * UnNormalizedFit.java - An interface of unnormalized fitness function
 */
package algorithms;

import java.util.concurrent.Callable;

/**
 * UnNormalizedFit是核心计算单元。你应该把它作为适应度函数来实现。
 *
 */
@SuppressWarnings("rawtypes")
public abstract class CoUnNormalizedFit implements Callable{
	protected Chromosome individual;
	protected Chromosome[] representatives;
	protected int subPop;

	public CoUnNormalizedFit(int subPop, Chromosome individual, Chromosome[] representatives){
		this.individual = individual;
		this.subPop = subPop;
		this.representatives = representatives;
	}
}
