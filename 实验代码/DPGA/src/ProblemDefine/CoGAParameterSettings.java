package ProblemDefine;
import java.util.Arrays;

public class CoGAParameterSettings {
    private double[] mutationRates, crossoverRates, lbounds, ubounds;
    private int optimization, maxGens, numOfSubPop;
	private int[] tournamentSizes, elitSizes, popSizes,
                 maxVars;

	public CoGAParameterSettings(
						double[] mutationRates,
						double[] crossoverRates,
						double[] lbounds,
						double[] ubounds,
						int[] tournamentSizes,
						int[] elitSizes,
						int[] popSizes,
						int[] maxVars,
						int numOfSubPop,
						int optimization,
						int maxGens
						) {
		this.mutationRates = mutationRates;
		this.crossoverRates = crossoverRates;
		this.lbounds = lbounds;
		this.ubounds = ubounds;
		this.tournamentSizes = tournamentSizes;
		this.elitSizes = elitSizes;
		this.optimization = optimization;
		this.popSizes = popSizes;
		this.maxGens = maxGens;
		this.maxVars = maxVars;
		this.numOfSubPop = numOfSubPop;
	}

	public int getNumOfSubPop(){
		return numOfSubPop;
	}
	public int[] getMaxVar(){
		return maxVars;
	}
	public double[] getLbound() {
		return lbounds;
	}
	public double[] getUbound() {
		return ubounds;
	}
	public int getOptimization() {
		return optimization;
	}
	public int[] getPopSize() {
		return popSizes;
	}
	public int getMaxGen() {
		return maxGens;
	}

	public double[] getMutationRate() {
		return mutationRates;
	}

	public double[] getCrossoverRate() {
		return crossoverRates;
	}

	public int[] getTournamentSize() {
		return tournamentSizes;
	}

	public int[] getElitSize() {
		return elitSizes;
	}
}
