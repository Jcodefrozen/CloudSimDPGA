package algorithms;

import java.util.ArrayList;

/**
 *定义了协同评价的接口
 */

public interface CoEvaluate {

    /**
     *
     * @param subPop 指示当前子种群的索引
     * @param popVar 当前种群
     * @param representatives 是代表(pBests)
     * @param popFit 适应度值
     */
    public void evaluate(int subPop, Chromosome[] popVar,
                         Chromosome[] representatives, ArrayList<double[]> popFit);

}
