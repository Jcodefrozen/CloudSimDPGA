/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * SinglePointCrossover.java - an implementation of crossover operator
 */
package commonOperators;

import java.lang.reflect.InvocationTargetException;

import algorithms.Chromosome;
import algorithms.Gene;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;

public class SinglePointCrossover implements TwoParentsCrossover {
    /**
     * 根据个人最好成绩更新全局最好成绩
     * 步骤:
     * 如果随机数大于交叉速率，则返回相同的父结点。
     * 首先得到染色体的子类型。
     * 获取允许组合两个基因的构造函数
     * 把染色体切成碎片，然后重建两个孩子
     * 返回子数组
     * @param father 选中的染色体
     * @param mother 选中的染色体
     * @param crossoverRate 交叉概率
     * @return 一组子染色体
     */
	@Override
	public Chromosome[] update(
					Chromosome father, 
					Chromosome mother, 
					double crossoverRate
					) {
		Chromosome[] children = new Chromosome[2];
		/* 如果随机数大于交叉概率。不要交叉。 */
		if(StdRandom.uniform() > crossoverRate) {
			children[0] = father.clone();
			children[1] = mother.clone();
			return children;
		}
		/* 随机选择割点*/
		int cutPoint = StdRandom.uniform(father.size());
		
		/* 得到染色体的类型 */
		Class<? extends Chromosome> childrenType = father.getClass();
		try {
		/* 使用构造函数生成两个子元素*/
			Chromosome child1 = childrenType
									.getConstructor(Gene.class, Gene.class)
									.newInstance(
												father.cut(cutPoint, 0), 
												mother.cut(cutPoint, 1)
												);
			Chromosome child2 = childrenType
									.getConstructor(Gene.class, Gene.class)
									.newInstance(
												mother.cut(cutPoint, 0),
												father.cut(cutPoint, 1)
												); 
			children[0] = child1;
			children[1] = child2;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return children;
	}

	
}
