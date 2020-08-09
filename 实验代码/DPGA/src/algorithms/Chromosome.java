/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Chromosome.java 	The interface of chromosome. You can define your own chromosome 
 * by implementing this interface.
 */

package algorithms;

public abstract class Chromosome implements Cloneable{
	/** 染色体大小 */
	public abstract int size();
	
	/** 用于交叉操作的cut操作
	 * @param cutPoint 切割点
	 * @param geneIndicator 染色体要返回的部分
	 * 切割点前的部分，1表示切割点后的部分
	 */
	public abstract Gene cut(int cutPoint, int geneIndicator);
	
	/**打印染色体 */
	public abstract void print();

	/** 
	 * 获取当前染色体的提取拷贝,
	 * 
	 * 请注意! ! !必须提供深度拷贝，或者方法依赖于此方
	 * 
	 */
	@Override
	public abstract Chromosome clone();

	public abstract boolean equals(Chromosome target);
}
