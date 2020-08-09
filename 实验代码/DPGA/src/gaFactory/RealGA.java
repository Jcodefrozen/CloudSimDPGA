/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * RealGA.java - An real value version GA
 */

package gaFactory;
import ProblemDefine.ParameterSettings;
import ProblemDefine.ProblemParameterSettings;
import java.util.ArrayList;

import GAProcedure.CommonGA;;

public class RealGA extends CommonGA{
	GAFactory factory;
	ParameterSettings pars;
	ProblemParameterSettings proSet;


	/**
	 * Constructor
	 * @param pars Parameter settings
	 * @param proSet Problem settings
	 * @param factory
	 */
	public RealGA(ParameterSettings pars, ProblemParameterSettings proSet, GAFactory factory){
		this.factory = factory;
		this.pars = pars;
		this.proSet = proSet;
		prepare();
	}


	@Override
	protected void prepare(){
		maxGen = pars.getMaxGen();
		maxVar = pars.getMaxVar();
		popSize = pars.getPopSize();
		mutationRate = pars.getMutationRate();
		crossoverRate = pars.getCrossoverRate();
		lbound = pars.getLbound();
		ubound = pars.getUbound();
		optimization = pars.getOptimization();
		tournamentSize = pars.getTournamentSize();
		elitSize = pars.getElitSize();
		popFit = new ArrayList<double[]>();

		initPop = factory.getInitPopMethod();
		mutation = factory.getMutation();
		crossover = factory.getCrossover();
		selection = factory.getSelection(tournamentSize, optimization);
		elitism = factory.getElitism(elitSize, optimization);
		evaluate = proSet.getEvaluate();
		collector = factory.getDataCollector();
		sort = factory.getSort();
	}
}
