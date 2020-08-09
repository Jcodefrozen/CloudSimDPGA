/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CrowdingDistance.java
 */
package multi_objective_operators;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Distance;

public class CrowdingDistance implements Distance {
	private int optimization;
	
	/**
	 * @param optimization 0 表示最小化, 1 表示最大化
	 */
	public CrowdingDistance(int optimization){
		this.optimization = optimization;
	}
	
	/**
	 * 
	 */
	public void calculate(Chromosome[] popVar, ArrayList<double[]> popFit){
		int ranking = 0, index = 0, lastIndex = 0;
		int popSize = popVar.length;
		Chromosome[] newPop = new Chromosome[popSize];
		while(true){
			if(index == popFit.size()){
				_calculate(popFit, index, lastIndex);
				break;
			}
			
			// 当前的染色体与前一个有不同的排名，因此，调整lastIndex的标记
			if(ranking != (int) popFit.get(index)[4]){
				ranking = (int) popFit.get(index)[4];
				_calculate(popFit, index, lastIndex);
				lastIndex = index;
			}
			index++;
		}
		//调整种群的顺序和染色体的指数
		for(int i = 0; i < popSize; i++){
			newPop[i] = popVar[(int) popFit.get(i)[2]];
			popFit.get(i)[2] = i;
		}
		popVar = newPop;
	}

	private void _calculate(ArrayList<double[]> popFit, int index, int lastIndex) {
		ArrayList<double[]> innerPopFit = new ArrayList<double[]>();
		// 新建一个popFit，确保它不会影响到旧的popFit
		for(int i = lastIndex; i < index; i++) {
			innerPopFit.add(popFit.get(i).clone());
		}

		if(innerPopFit.size() == 0) return;
		// 如果只有一个实例，分配无限值
		if(innerPopFit.size() == 1) {
			popFit.get(lastIndex)[3] = Double.POSITIVE_INFINITY;
			return;
		}
		//如果有两个实例，给它们赋无穷大的值
		if(innerPopFit.size() == 2) {
			popFit.get(lastIndex)[3] = Double.POSITIVE_INFINITY;
			popFit.get(lastIndex + 1)[3] = Double.POSITIVE_INFINITY;
			return;
		}
		
		// 根据适合度值对第一个目标进行排序。
		Collections.sort(innerPopFit, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[0] - fitness2[0] > 0.0) condition = 1;
				else if(fitness1[0] - fitness2[0] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});
		// 如果优化是相反的，则反转列表
		if(optimization == 1) Collections.reverse(innerPopFit);
		
		// 获取边缘上的值，以进行标准化
		double objectiveMaxn = innerPopFit.get(0)[0];
		double objectiveMinn = innerPopFit.get(innerPopFit.size() - 1)[0];
		
		// 给边缘染色体赋无穷值
		innerPopFit.get(0)[3] = Double.POSITIVE_INFINITY;
		innerPopFit.get(innerPopFit.size() - 1)[3] = Double.POSITIVE_INFINITY;
		
		
		// 计算人与人之间的距离
		for(int j = 1; j < innerPopFit.size() - 1; j++){
			double distance = innerPopFit.get(j + 1)[0] - innerPopFit.get(j - 1)[0];
			distance = distance / Math.abs(objectiveMaxn - objectiveMinn);
			if(distance == distance)
				innerPopFit.get(j)[3] = distance;
		}
		
		// 根据适合度值对第二个目标进行排序。
		Collections.sort(innerPopFit, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[1] - fitness2[1] > 0.0) condition = 1;
				else if(fitness1[1] - fitness2[1] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});
		if(optimization == 1) Collections.reverse(innerPopFit);
		
		// 获取边缘上的值，以进行标准化
		objectiveMaxn = innerPopFit.get(0)[1];
		objectiveMinn = innerPopFit.get(innerPopFit.size() - 1)[1];
	
		// a分配无穷大的值
		innerPopFit.get(0)[3] = Double.POSITIVE_INFINITY;
		innerPopFit.get(innerPopFit.size() - 1)[3] = Double.POSITIVE_INFINITY;
		
		// 计算个体距离
		for(int j = 1; j < innerPopFit.size() - 1; j++){
			double distance = innerPopFit.get(j + 1)[1] - innerPopFit.get(j - 1)[1];
			// normalize
			distance = distance / Math.abs(objectiveMaxn - objectiveMinn);
			// if the max == min, the objective's distance is discarded
			if(distance == distance)
				innerPopFit.get(j)[3] += distance;
		}

		Collections.sort(innerPopFit, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[3] - fitness2[3] > 0.0) condition = 1;
				else if(fitness1[3] - fitness2[3] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});	
		Collections.reverse(innerPopFit);

		for(int i = lastIndex, j = 0; i < index; i++, j++) 
			popFit.set(i, innerPopFit.get(j));
	}
	
}
