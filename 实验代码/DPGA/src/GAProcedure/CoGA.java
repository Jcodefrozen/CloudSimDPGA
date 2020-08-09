package GAProcedure;

import algorithms.Chromosome;
import algorithms.Coevolution;
import algorithms.TwoParentsCrossover;
import algorithms.StdRandom;

public abstract class CoGA extends Coevolution {
    /**
     * 步骤:
     * 启动计时器
     * 1. 初始化子种群
     * 对于每一次迭代
     * 2. 评估每个种群，收集最佳适应度值
     * 3.排序种群
     * 4. 选择父代
     * 5. 交叉
     * 6. 给子代打分
     * 7. 变异
     * 8. 结束计时器
     * </ul>
     *
     * @param seed 随机种子值
     */
    @Override
    public void run(int seed) {
        collector.collectTime(0);
        initializeRand(seed);

        /**
         *
         * 对于每个子种群,
         * 1. 初始化他们的种群,
         * 2. 初始化他们的代表
         * 3. 初始化它们的代表性适应度值
         *
         */
        for (int i = 0; i < numOfSubPop; i++) {
            popVars.add(initPops[i].init(popSizes[i], maxVars[i], lbounds[i], ubounds[i]));

            //在第一代中随机选择一个人作为代表
            int u = StdRandom.uniform(popSizes[i]);
            representatives[i] = popVars.get(i)[u].clone();
        }

        /**
         * 对每一代
         */
        for (int genCount = 0; genCount < maxGen; genCount++) {

            /**
             * 对于每个子群体，做以下工作:
             */
            for (int subPop = 0; subPop < numOfSubPop; subPop++) {
                Chromosome[] newPop = new Chromosome[popSizes[subPop]];
                int childrenCount = elitisms[subPop].getSize();
                try {
                    /**
                     * Evaluate
                     * 评估需要四个参数
                     * @param subPop 子种群的数量
                     * @param popVars 种群
                     * @param representatives 显示出最好的个体
                     * @param popFits[subPop] 适应度
                     */
                    evaluate.evaluate(subPop, popVars.get(subPop),
                                      representatives, popFits[subPop]);
                } catch (Exception e){
                    e.printStackTrace();
                }
                sorts[subPop].sort(popVars.get(subPop), popFits[subPop]);

                representatives[subPop] = popVars.get(subPop)[0];
                // popFits is an array of ArrayList<double[]>
                // Because we have sorted the popFits, therefore, the best fitness value should be
                // at the top. double[fitness, ranking]. Therefore, we need the index 0.
                repFits[subPop] = popFits[subPop].get(0)[0];
                elitisms[subPop].carryover(popVars.get(subPop), newPop);
                // If it is the last sub-pop, then the fitness value should be collected
                // We collect the chromosome and its fitness value
                if(subPop == numOfSubPop - 1) {
                    collector.collect(popFits[subPop].get(0)[0]);
                    collector.collectSet(representatives);
                }

                while(true) {
                    int exitFlag = 0;
                    System.out.print("Sub-pop: " + subPop + " ");
                    Chromosome father = popVars.get(subPop)[selections[subPop].selected(popVars.get(subPop), popFits[subPop])];
                    Chromosome mother = popVars.get(subPop)[selections[subPop].selected(popVars.get(subPop), popFits[subPop])];
                    // 生成子代
                    Chromosome[] children =  ((TwoParentsCrossover) crossovers[subPop])
										.update(father, mother, crossoverRates[subPop]);

                    // 变异
                    for(int j = 0; j < children.length; j++) {
                        mutations[j].update(children[j], mutationRates[subPop]);
                        newPop[childrenCount] = children[j].clone();
                        childrenCount++;
                        if(childrenCount == popSizes[subPop]){
                            exitFlag = 1;
                            break;
                        }
                    }
                    if(exitFlag == 1) break;
                }
                popVars.set(subPop, newPop.clone());
            }
            collector.collectTime(1);
        }
    } // run ends

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
