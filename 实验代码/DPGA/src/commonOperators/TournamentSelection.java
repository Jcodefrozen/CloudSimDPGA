/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * TournamentSelection.java - an implementation of tournament selection.
 */
package commonOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Selection;
import algorithms.StdRandom;

public class TournamentSelection implements Selection{
	private int tournamentSize, optimization;

	/**
	 * Constructor
	 * @param tournamentSize size of tournament, larger number makes it harder to select a worse individual
	 * @param optimization 0 denotes minimize, 1 denotes maximize
	 */
	public TournamentSelection(int tournamentSize, int optimization, int seed) {
		this.tournamentSize = tournamentSize;
		this.optimization = optimization;
		StdRandom.setSeed(seed);
	}
	/**
	 * 锦标赛选择步骤:
	 * 1. 从人群中随机选择染色体的锦标赛大小
	 * 2.找到最好的一个并返回它在总体中的索引
	 * @param popVar population
	 * @param fitness fitness list
	 *
	 */
	@Override
	public int selected(Chromosome[] popVar, ArrayList<double[]> fitness) {
		double[] greatestFit = new double[2];
		ArrayList<double[]> chosen = new ArrayList<double[]>();
		for(int i = 0; i < tournamentSize; i++){
			int r = StdRandom.uniform(fitness.size());
			chosen.add(fitness.get(r));
		}


		Collections.sort(chosen, new Comparator<double[]>() {
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[0] - fitness2[0] > 0.0) condition = 1;
				else if(fitness1[0] - fitness2[0] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
		});
		greatestFit[0] = chosen.get(0)[0];
		greatestFit[1] = chosen.get(0)[1];


//		System.out.println("greatestFit[0] = " + greatestFit[0] + ", greatestFit[1] = " + greatestFit[1]);
		return (int) greatestFit[1];
	}

}
