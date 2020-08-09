/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ResultCollector.java
 */
package Rosenbrock;

import java.util.ArrayList;

import algorithms.Chromosome;
import dataCollector.DataCollector;

public class ResultCollector extends DataCollector {
	private ArrayList<double[]> resultData;
	private ArrayList<Chromosome[]> data;

	/**
	 * 构造函数,初始化字段
	 */
	public ResultCollector(){
		resultData = new ArrayList<double[]>();
		data = new ArrayList<Chromosome[]>();
	}
	@Override
	/**
	 * collect()将在默认情况下收集适合度值
	 */
	public void collect(Object data) {
		resultData.add((double[]) data);
	}

	public ArrayList<double[]> getResult(){
		return resultData;
	}


	public void printResult(){
		for(int i = 0; i < resultData.size(); i++){
			System.out.println("fitness: " + resultData.get(i)[0]);
		}
		System.out.println();
	}

	public void printPop(){
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.get(0).length; j++) {
				data.get(i)[j].print();
				System.out.println();
			}
			System.out.println();
		}
	}

	/** 打印每次运行的最后一个适应度值 */
	public void printBestInRuns(int gen){
		for(int i = gen - 1; i < resultData.size(); i += gen){
			System.out.println("fitness: " + resultData.get(i)[0]);
		}
	}

	public void collectChromosome(Chromosome[] popVar) {
		data.add(popVar);
	}

	@Override
	public void collectSet(Object set) {
		// TODO Auto-generated method stub

	}


}
