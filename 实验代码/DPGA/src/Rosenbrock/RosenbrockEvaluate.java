/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RosenbrockEvaluate.java evaluation function
 */
package Rosenbrock;

import java.util.ArrayList;

import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;

public class RosenbrockEvaluate implements Evaluate{
	private ArrayList<FitnessFunc> funcList;


	public RosenbrockEvaluate(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	public void setFuncList(ArrayList<FitnessFunc> funcList){
		this.funcList = funcList;
	}

	@Override

	public void evaluate(Chromosome[] popVar, ArrayList<double[]> popFit) {
		popFit.clear();
		
		//获取适应度函数列表，以防你有多个目标函数。
		FitnessFunc fitnessFunction = funcList.get(0);
		ArrayList<double[]> tempFit = null;
		try {
			// 执行评估
			tempFit = fitnessFunction.execute(popVar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for(int i = 0; i < tempFit.size(); i++) {
			popFit.add(tempFit.get(i));
		}
	}
}
