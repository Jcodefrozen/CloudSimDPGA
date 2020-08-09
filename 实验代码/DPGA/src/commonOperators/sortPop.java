/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * sortPop.java - A sort method for population
 */
package commonOperators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import algorithms.Chromosome;
import algorithms.Sort;
/**
 * 种群排序
 */
public class sortPop implements Sort{
	/**
	 * @param popVar population
	 * @param popFit fitness
	 */
	@Override
	public void sort(Chromosome[] popVar, ArrayList<double[]> popFit){
		Chromosome[] newPop = new Chromosome[popVar.length];

		try {
		Collections.sort(popFit, new Comparator<double[]>() {
			
			@Override
			public int compare(double[] fitness1, double[] fitness2) {
				int condition = 0;
				if(fitness1[0] - fitness2[0] > 0.0) condition = 1;
				else if(fitness1[0] - fitness2[0] < 0.0) condition = -1;
				else condition = 0;
				return condition;
			}
	
		});
		}catch(IllegalArgumentException e){
			System.out.println("Sorting problem occurs!");
		}
		
		for(int i = 0; i < popVar.length; i++){
			newPop[i] = popVar[(int) popFit.get(i)[1]];
			popFit.get(i)[1] = i;
		}
		for(int i = 0; i < popVar.length; i++){
			popVar[i] = newPop[i];
		}
	}
}
