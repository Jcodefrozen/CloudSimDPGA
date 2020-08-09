/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BinaryTournamentSelection.java - A binary tournament selection procedure
 */
package multi_objective_operators;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.StdRandom;
import commonOperators.TournamentSelection;

public class BinaryTournamentSelection extends TournamentSelection{

	public BinaryTournamentSelection(int optimization, int seed) {
		super(2, optimization, seed);
	}

	@Override
	/**
	 * 选择两个染色体的适应度值
	 * 适应度值如下
	 * 	[0] obj1 value
	 *  [1] obj2 value
	 *  [2] position in the population
	 *  [3] crowding distance
	 *  [4] rank
	 *  [5] number of violations
	 * 
	 * 这个实现使用了排序控制来选择二进制中更好的染色体。它首先考虑违规的数量。违反次数较少的染色体总是会被选中。
	 * 排序越靠前的染色体越好。
	 * 如果两个染色体排列相同，拥挤距离越小的染色体越好
	 */
	public int selected(Chromosome[] popVar, ArrayList<double[]> fitness) {
		double[] rank 				= new double[2];
		double[] crowdingDistance 	= new double[2];
		double[] violations			= new double[2];
		ArrayList<double[]> chosen 	= new ArrayList<double[]>();
		int[] chosenIndex			= new int[2];

		// randomly choose two chromosomes
		chosenIndex[0] = StdRandom.uniform(fitness.size());
		chosen.add(fitness.get(chosenIndex[0]));

		chosenIndex[1] = StdRandom.uniform(fitness.size());
		chosen.add(fitness.get(chosenIndex[1]));

		rank[0] 				= chosen.get(0)[4];
		rank[1] 				= chosen.get(1)[4];
		crowdingDistance[0] 	= chosen.get(0)[3];
		crowdingDistance[1] 	= chosen.get(1)[3];
		violations[0]		= chosen.get(0)[5];
		violations[1]		= chosen.get(1)[5];

		if(violations[0] < violations[1]){
			return chosenIndex[0];
		} else if(violations[0] > violations[1]){
			return chosenIndex[1];
		} else {
			if(rank[0] < rank[1]){
				return chosenIndex[0];
			} else if(rank[0] > rank[1]){
				return chosenIndex[1];
			} else{
				if(crowdingDistance[0] > crowdingDistance[1]){
					return chosenIndex[0];
				} else {
					return chosenIndex[1];
				}
			}
		}
	} // selected ends
//
//	@Override
//	public int selected(Chromosome[] popVar, ArrayList<double[]> fitness) {
//		double[] rank 				= new double[2];
//		double[] crowdingDistance 	= new double[2];
//		double[] violations			= new double[2];
//		ArrayList<double[]> chosen 	= new ArrayList<double[]>();
//		int[] chosenIndex			= new int[2];
//
//		// randomly choose two chromosomes
//		chosenIndex[0] = StdRandom.uniform(fitness.size());
//		chosen.add(fitness.get(chosenIndex[0]));
//
//		chosenIndex[1] = StdRandom.uniform(fitness.size());
//		chosen.add(fitness.get(chosenIndex[1]));
//
//		rank[0] 				= chosen.get(0)[4];
//		rank[1] 				= chosen.get(1)[4];
//		crowdingDistance[0] 	= chosen.get(0)[3];
//		crowdingDistance[1] 	= chosen.get(1)[3];
//		violations[0]		= chosen.get(0)[5];
//		violations[1]		= chosen.get(1)[5];
//
////		if(violations[0] < violations[1]){
////			return chosenIndex[0];
////		} else if(violations[0] > violations[1]){
////			return chosenIndex[1];
////		} else {
//			if(rank[0] < rank[1]){
//				return chosenIndex[0];
//			} else if(rank[0] > rank[1]){
//				return chosenIndex[1];
//			} else{
//				if(crowdingDistance[0] > crowdingDistance[1]){
//					return chosenIndex[0];
//				} else {
//					return chosenIndex[1];
//				}
//			}
////		}
//	} // selected ends


}
