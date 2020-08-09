/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective Genetic algorithm framework
 * Description:  Single-objective Genetic algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * IntValueChromosome.java - A real value chromosome representation
 */

package commonRepresentation;


import algorithms.Chromosome;
import algorithms.Gene;
public class IntValueChromosome extends Chromosome{
	public int [] individual;
	public int [][] matrixIndividual;

	public IntValueChromosome(int size){
		individual = new int[size];
	}
	/**
	 * Constructor
	 * 需要两个基因来构造染色体。在这种情况下，基因与染色体紧密结合。必须手动定义染色体和基因。
	 * 
	 * 它用两个基因片段构建了一个新的染色体。
	 * 
	 * @param firstPart
	 * @param secondPart
	 *
	 */
	public IntValueChromosome(Gene firstPart, Gene secondPart){
		individual = new int[firstPart.size() + secondPart.size()];
		for(int i = 0; i < firstPart.size(); i++) {
			individual[i] = ((IntGene) firstPart).gene[i];
		}
		for(int i = firstPart.size(), j = 0; j < secondPart.size(); j++, i++) {
			individual[i] = ((IntGene)secondPart).gene[j];
		}	
	}
	
	/** 得到染色体的大小 */
	@Override
	public int size() {
		return individual.length;
	}
	
	/**
	 *  @param cutPoint
	 *  @param geneIndicator
	 *  @return return gene part
	 */
	@Override
	public IntGene cut(int cutPoint, int geneIndicator){
		IntGene part;
		if(geneIndicator == 0) {
			part = new IntGene(cutPoint + 1); 
			for(int i = 0; i < cutPoint + 1; i++){
				part.gene[i] = individual[i];
			}
		} else {
			part = new IntGene(size() - (cutPoint + 1));
			for(int i = cutPoint + 1, j = 0; i < size(); i++, j++){
				part.gene[j] = individual[i];
			}
		}
		return part;
	}

	@Override
	public void print() {
		for(int i = 0; i < size(); i++){
			System.out.print(individual[i] + " ");
		}
		System.out.println();
	}
	

	public void printMatrix(){
		int rowNum = matrixIndividual.length;
		int colNum = matrixIndividual[0].length;
		for(int i = 0; i < rowNum; i++){
			for(int j = 0; j < colNum; j++){
				System.out.print(matrixIndividual[i][j] + " ");
			}
			System.out.println();
		}
	}
	

	public void toMatrix(int row){
		int chromoSize = size();
		int col = chromoSize / row;
		matrixIndividual = new int[row][col];
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				matrixIndividual[i][j] = individual[i * col + j];
			}
		}
	}

	@Override
	public IntValueChromosome clone() {
		IntValueChromosome copy = new IntValueChromosome(size());
		for(int i = 0; i < size(); i++){
			copy.individual[i] = individual[i];
		}
		return copy;
	}

	private boolean equals(IntValueChromosome target){
		int chromoSize = size();
		for(int i = 0; i < chromoSize; i++){
			if(individual[i] != target.individual[i]){
				return false;
			}
		}
		return true;
	}


	public void synchronizeMatrixToVector(){
		int chromoSize = size();
		int col = 0,row = 0;
		try {
			col = matrixIndividual[0].length;
			row = matrixIndividual.length;
		} catch(Exception e){
			System.out.println("Matrix does not exist!");
			return;
		}
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				individual[i * col + j] = matrixIndividual[i][j] ;
			}
		}
	}

	public void synchronizeVectorToMatrix() {
        int chromoSize = size();
        int col = 0,row = 0;
        try {
            col = matrixIndividual[0].length;
            row = matrixIndividual.length;
        } catch(Exception e){
            System.out.println("Matrix does not exist!");
            return;
        }
        for(int i = 0; i < row; i++){
            for(int j = 0; j < col; j++){
                matrixIndividual[i][j] = individual[i * col + j];
            }
        }
	}

	@Override
	public boolean equals(Chromosome target) {
		return equals((IntValueChromosome) target);
	}
}
