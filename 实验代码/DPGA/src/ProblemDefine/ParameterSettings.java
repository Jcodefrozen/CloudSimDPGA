/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * ParameterSettings.java - common GA parameter settings
 */
package ProblemDefine;

public class ParameterSettings {
	private double mutationRate, crossoverRate, lbound, ubound;
	private int optimization, tournamentSize, elitSize, popSize, maxGen, maxVar, seed;
	public ParameterSettings(
						double mutationRate,
						double crossoverRate,
						double lbound,
						double ubound,
						int tournamentSize,
						int elitSize,
						int optimization,
						int popSize,
						int maxGen,
						int maxVar,
						int seed
						){
		this.mutationRate = mutationRate;
		this.crossoverRate = crossoverRate;
		this.lbound = lbound;
		this.ubound = ubound;
		this.tournamentSize = tournamentSize;
		this.elitSize = elitSize;
		this.optimization = optimization;
		this.popSize = popSize;
		this.maxGen = maxGen;
		this.maxVar = maxVar;
		this.seed = seed;
	}

	public int getMaxVar(){
		return maxVar;
	}
	public double getLbound() {
		return lbound;
	}
	public double getUbound() {
		return ubound;
	}
	public int getOptimization() {
		return optimization;
	}
	public int getPopSize() {
		return popSize;
	}
	public int getMaxGen() {
		return maxGen;
	}

	public double getMutationRate() {
		return mutationRate;
	}

	public double getCrossoverRate() {
		return crossoverRate;
	}

	public int getTournamentSize() {
		return tournamentSize;
	}

	public int getSeed() {
		return seed;
	}

	public int getElitSize() {
		return elitSize;
	}
}
