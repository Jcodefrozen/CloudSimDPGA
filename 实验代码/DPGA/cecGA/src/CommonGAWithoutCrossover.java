/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CommonGA.java - An common procedure for GA
 */

import algorithms.Chromosome;
import algorithms.GeneticAlgorithm;
import algorithms.TwoParentsCrossover;

public abstract class CommonGAWithoutCrossover extends GeneticAlgorithm{
/**
 * 步骤:
 * 启动计时器
 * 	    1. 初始化种群
 * 		对于每一代
 * 		2. 评估人群，收集最佳适应度值
 * 		3.这种人口
 * 		 子代数量不够时
 * 		4. 选择父代
 * 	    5. 交叉
 * 	    6. 给子代打分
 * 		7. 变异
 * 	结束计时器
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
//			for(int j = 0; j < popSize; j++){
//				popVar[j].print();
//				System.out.println(popFit.get(j)[0]);
//			}
			elitism.carryover(popVar, newPop);
//			System.out.println("best fitness value = " + popFit.get(0)[0]);
//			System.out.println("best individual: ");
//			popVar[0].print();
//			System.out.println();
			collector.collect(popVar[0]);
			

			while(true) {
				int exitFlag = 0;
				Chromosome father = popVar[selection.selected(popVar, popFit)];
				Chromosome mother = popVar[selection.selected(popVar, popFit)];

//				Chromosome[] children = ((TwoParentsCrossover) crossover)
//										.update(father, mother, crossoverRate);
                Chromosome[] children = new Chromosome[2];
                children[0] = father.clone();
                children[1] = mother.clone();


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
	
	/**
	 * 重复实验
	 */
	@Override
	public void runNtimes(int seedStart, int nTimes) {
		for(int i = 0; i < nTimes; i++){
			run(seedStart);
			seedStart++;
		}
	}
}
