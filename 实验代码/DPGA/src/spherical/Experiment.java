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
package spherical;
import java.util.ArrayList;
import ProblemDefine.*;
import algorithms.Evaluate;
import algorithms.FitnessFunc;
import algorithms.GeneticAlgorithm;
import dataCollector.DataCollector;
import gaFactory.RealGA;
import gaFactory.RealGAFactory;

public class Experiment {

	public static void main(String[] arg) {

		int seed = 2333;


		ArrayList<FitnessFunc> funcList = new ArrayList<FitnessFunc>();

		double perturbation = 20;

		double mutationRate = 0.1;
		/** 变异概率 */
		double crossoverRate = 0.7;

		double lbound = -100; // ranging in [-100, 300]
		double ubound = 100;
		
		/** 锦标赛选择大小 */
		int tournamentSize = 10;
		
		/** 精英策略 */
		int elitSize = 10;

		int optimization = 0; //minimize
		
		/** 种群大小 */
		int popSize = 50;
		
		/** 迭代数 */
		int maxGen = 100;
		int d = 20; // number of dimensions

		/** 适应度函数优化 */
		FitnessFunc fitnessFunction = new FitnessFunc(SphericalUnNormalizedFit.class);
		funcList.add(fitnessFunction);


		Evaluate evaluate = new SphericalEvaluate(funcList);


		ProblemParameterSettings proSet = new SphericalParameterSettings(evaluate, null);
		

		ParameterSettings pars = new ParameterSettings(
								mutationRate, crossoverRate, lbound,
								ubound, tournamentSize, elitSize, optimization,
								popSize, maxGen, d, seed);
		
		/** set up DataCollector */
		DataCollector collector = new ResultCollector();

		/** select a type of genetic algorithm, initialize it with a factory */
		GeneticAlgorithm myAlg = new RealGA(pars, proSet, new RealGAFactory(
				collector, lbound,
				ubound, perturbation, seed));




//		myAlg.run(233333); // parameter is a random seed
		myAlg.runNtimes(23333, 30);
		

		((ResultCollector) collector).printBestInRuns(maxGen);
		((ResultCollector) collector).printMeanTime();;

//		((ResultCollector) collector).printPop();
		System.out.println("Done!");
	}
}
