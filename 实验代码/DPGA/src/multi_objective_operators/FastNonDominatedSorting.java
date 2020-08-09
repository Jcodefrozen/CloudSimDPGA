/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FastNonDominatedSorting.java
 */
package multi_objective_operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Sort;

public class FastNonDominatedSorting implements Sort{
	private int optimization;
	
	public FastNonDominatedSorting(int optimization){
		this.optimization = optimization;
	}
	
	/**
	 * Sort
	 * Steps
	 *  1.对每个染色体，计算其支配数和支配集。
	 *  2.根据排列顺序插入染色体，直到所有的染色体都已插入
	 *  3.根据他们的排名排序
	 * @param popVar population
	 * @param popFit fitness
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit) {
		int ranking = 0;
		Chromosome[] newPop = new Chromosome[popVar.length];
		ArrayList<Integer> dominationCount = new ArrayList<Integer>();
		ArrayList<ArrayList<double[]>> dominatedSet = new ArrayList<ArrayList<double[]>>();
		ArrayList<double[]> sortedList = new ArrayList<double[]>();
		
		// 对每个染色体，计算其支配数和支配集
		for(int i = 0; i < popFit.size(); i++){
			ArrayList temp = count(i, popFit, popVar.length);
			// 0 支配数量
			dominationCount.add((Integer) temp.get(0));
			
			// 1 支配集
			dominatedSet.add((ArrayList<double[]>) temp.get(1));
		}
		
		// 根据排列顺序插入染色体，直到所有的染色体都已插入。
		while(true){
			ArrayList<double[]> zeroRankList = new ArrayList<double[]>();
			//所有的适应度都已排序完毕，终止
			if(sortedList.size() == popVar.length) break;
			for(int i = 0; i < popVar.length; i++){
				if(dominationCount.get(i) == 0){
					popFit.get(i)[4] = ranking;
					zeroRankList.add(popFit.get(i));
					dominationCount.set(i, -1);
				}
			}
			ranking++;

			for(int j = 0; j < zeroRankList.size(); j++){
				
				ArrayList<double[]> dominatedChromosomes = dominatedSet.get((int) zeroRankList.get(j)[2]);
				for(int k = 0; k < dominatedChromosomes.size(); k++){
					// 每个显性染色体的显性计数为-1
//					dominationCount.set((int) chr[2], dominationCount.get((int) chr[2]) - 1);
					// 得到显性染色体的指标
					int dominatedChromosomeIndex = (int) dominatedChromosomes.get(k)[2];
					dominationCount.set(dominatedChromosomeIndex, dominationCount.get(dominatedChromosomeIndex) - 1);
				}
			}
			
			// 根据拥挤距离排序。
			Collections.sort(zeroRankList, new Comparator<double[]>() {
				@Override
				public int compare(double[] fitness1, double[] fitness2) {
					int condition = 0;
					if(fitness1[3] - fitness2[3] > 0.0) condition = 1;
					else if(fitness1[3] - fitness2[3] < 0.0) condition = -1;
					else condition = 0;
					return condition;
				}
			});		
			// 对于拥挤距离，越大越好。因此，我们使用后代顺序。
			Collections.reverse(zeroRankList);
			sortedList.addAll(zeroRankList);
		}

		Collections.sort(sortedList, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[4] - fitness2[4] > 0.0) condition = 1;
				else if(fitness1[4] - fitness2[4] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});

		for(int i = 0; i < popVar.length; i++){
			newPop[i] = popVar[(int) sortedList.get(i)[2]];
			sortedList.get(i)[2] = i;
		}

		for(int i = 0; i < popVar.length; i++){
			popVar[i] = newPop[i];
		}
		popFit.clear();
		popFit.addAll(sortedList);
	}
	
	/**
	 * 计算它的支配数，只支持两个目标。
	 * 
	 * @param index
	 * @param popFit 适应度值
	 * @return 支配数和当前染色体的支配集
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList count(int index, ArrayList<double[]> popFit, int popSize){
		double[] currentFitness = popFit.get(index);
		// dominatedList是由当前染色体决定的染色体适应度列表
		ArrayList<double[]> dominatedList = new ArrayList<double[]>();
		
		
		Integer dominatedNo = 0;
		for(int i = 0; i < popSize; i++){
			if(i == index) continue;
			double[] targetFitness = popFit.get(i);
			// 计算当前占主导地位的染色体的数目
			if((optimization == 0 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1])){
				// 如果两个染色体具有相同的适应度，它们就不是显性的
				if(targetFitness[0] == currentFitness[0] && targetFitness[1] == currentFitness[1]){
				} else {
					dominatedNo++;
				}
			}
			
			//将当前一个域的染色体放入一个ArrayList中
			if((optimization == 0 && targetFitness[0] >= currentFitness[0] && targetFitness[1] >= currentFitness[1]) ||
				(optimization == 1 && targetFitness[0] <= currentFitness[0] && targetFitness[1] <= currentFitness[1])){
				if(targetFitness[0] == currentFitness[0] && targetFitness[1] == currentFitness[1]){
				} else {
					dominatedList.add(targetFitness);
				}
			}
		}

		ArrayList temp = new ArrayList();
		temp.add(dominatedNo);
		temp.add(dominatedList);
		return temp;
	}
	

}
