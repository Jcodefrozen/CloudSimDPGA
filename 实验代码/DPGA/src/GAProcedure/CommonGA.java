/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CommonGA.java - An common procedure for GA
 */

package GAProcedure;

import algorithms.Chromosome;
import algorithms.GeneticAlgorithm;
import algorithms.TwoParentsCrossover;

public abstract class CommonGA extends GeneticAlgorithm{
/**
 * 
 * @param seed Random seed
 */
	@Override
	public void run(int seed){
		collector.collectTime(0);
		initializeRand(seed);
		popVar = initPop.init(popSize, maxVar, lbound, ubound);

		for(int i = 0; i < maxGen; i++){
			int childrenCount = elitism.getSize();
			Chromosome[] newPop = new Chromosome[popSize];
			try {
				evaluate.evaluate(popVar, popFit);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			sort.sort(popVar, popFit);
			elitism.carryover(popVar, newPop);
//			System.out.println("best fitness value = " + popFit.get(0)[0]);
//			System.out.println("best individual: ");
//			popVar[0].print();
//			System.out.println();
//			collector.collect(popFit.get(0));
			collector.collect(popVar[0]);

			while(true) {
				int exitFlag = 0;
				Chromosome father = popVar[selection.selected(popVar, popFit)];
				Chromosome mother = popVar[selection.selected(popVar, popFit)];

				Chromosome[] children = ((TwoParentsCrossover) crossover)
										.update(father, mother, crossoverRate);


				for(int j = 0; j < children.length; j++) {
					mutation.update(children[j], mutationRate);
					newPop[childrenCount] = children[j].clone();
					childrenCount++;
					if(childrenCount == popSize) {
						exitFlag = 1;
						break;
					}
				}
				if(exitFlag == 1) break;
			}				
			popVar = newPop.clone();
		}
		collector.collectTime(1);
	}

	@Override
	protected abstract void prepare();

	@Override
	public void runNtimes(int seedStart, int nTimes) {
		for(int i = 0; i < nTimes; i++){
			run(seedStart);
			seedStart++;
		}
	}
}
