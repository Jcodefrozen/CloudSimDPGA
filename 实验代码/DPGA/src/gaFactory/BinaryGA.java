/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BinaryGA.java - A binary version GA
 */
package gaFactory;

import java.util.ArrayList;

import GAProcedure.CommonGA;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;

public class BinaryGA extends CommonGA {
	private GAFactory factory;
	private ParameterSettings pars;
	private ProblemParameterSettings proSet;

	/**
	 * Constructor
	 * @param pars Parameter settings
	 * @param proSet Problem settings
	 * @param factory factory is used to assemble parts
	 */	
	public BinaryGA(
					ParameterSettings pars, 
					ProblemParameterSettings proSet, 
					GAFactory factory
					){
		this.factory = factory;
		this.pars = pars;
		this.proSet = proSet;
		prepare();
	}
	@Override
	/**
	 *
	 * maxGen: 		最大迭代数
	 * maxVar: 		一个粒子的最大变量数
	 * popSize:		种群大小
	 * optimization 最大化(1) 或最小化 (0)
	 * popFit		population fitness
	 * initPop		种群初始化
	 * mutation		变异方法
	 * crossover 	交叉方法
	 * selection	选择方法
	 * evaluate		评估
	 * collector	data collector
	 * sort   		排序
	 */
	protected void prepare() {
		maxGen = pars.getMaxGen();
		maxVar = pars.getMaxVar();
		popSize = pars.getPopSize();
		mutationRate = pars.getMutationRate();
		crossoverRate = pars.getCrossoverRate();
		optimization = pars.getOptimization();
		tournamentSize = pars.getTournamentSize();
		elitSize = pars.getElitSize();
		popFit = new ArrayList<double[]>();

		initPop = factory.getInitPopMethod();
		mutation = factory.getMutation();
		crossover = factory.getCrossover();
		elitism = factory.getElitism(elitSize, optimization);
		selection = factory.getSelection(tournamentSize, optimization);
		evaluate = proSet.getEvaluate();
		collector = factory.getDataCollector();
		sort = factory.getSort();	
	}

}
