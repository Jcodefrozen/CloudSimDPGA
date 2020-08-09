/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * Experiment.java 	The main function.
 * 
 */
package Rosenbrock;
import java.util.ArrayList;
import ProblemDefine.*;
import algorithms.Evaluate;
import algorithms.FitnessFunc;
import algorithms.GeneticAlgorithm;
import dataCollector.DataCollector;
import gaFactory.RealGA;
import gaFactory.RealGAFactory;

public class Experiment {
	/**
	 * 初始化所有参数，建立评价函数。运行算法。打印结果。
	 * 
	 */
	public static void main(String[] arg) {

		int seed = 23333;

		ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();
		/** 在多项式突变中使用变异，多项式突变是一个实值突变(建议值在20到100之间)，如果你的染色体是INT值，那么不用担心。 */
		double perturbation = 20;
		double mutationRate = 0.1;
		double crossoverRate = 0.7;
		
		double lbound = -30; // ranging in [-30, 30]
		double ubound = 30;
		
		int tournamentSize = 10;
		
		int elitSize = 10;
		
		int optimization = 0; //minimize
		
		int popSize = 50;
		
		int maxGen = 100;
		int d = 20; // number of dimensions

		/** 适应度函数进行初始化*/
		FitnessFunc fitnessFunction = new FitnessFunc(RosenbrockUnNormalizedFit.class);
		funcList.add(fitnessFunction);

		/** 将适应度函数注册到您的评估函数中*/
		Evaluate evaluate = new RosenbrockEvaluate(funcList);

		/** 在问题参数设置中注册求值*/
		ProblemParameterSettings proSet = new RosenbrockParameterSettings(evaluate, null);
		
		/** 初始化算法参数 */
		ParameterSettings pars = new ParameterSettings(
									mutationRate, crossoverRate, lbound,
									ubound, tournamentSize, elitSize, optimization,
									popSize, maxGen, d, seed);

		DataCollector collector = new ResultCollector();

		/** 选择一种类型的遗传算法，用工厂初始化它 */
		GeneticAlgorithm myAlg = new RealGA(pars, proSet, new RealGAFactory(
																collector, lbound,
																ubound, perturbation, seed));




//		myAlg.run(233333); // parameter is a random seed
		myAlg.runNtimes(233333, 30);
		

		((ResultCollector) collector).printBestInRuns(maxGen);
		((ResultCollector) collector).printMeanTime();

//		((ResultCollector) collector).printPop();
		System.out.println("Done!");
	}
}
