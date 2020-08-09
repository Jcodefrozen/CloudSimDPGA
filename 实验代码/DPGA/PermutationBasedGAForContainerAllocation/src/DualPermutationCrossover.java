import algorithms.Chromosome;
import algorithms.StdRandom;
import algorithms.TwoParentsCrossover;

public class DualPermutationCrossover implements TwoParentsCrossover{

    public DualPermutationCrossover(int seed){
        StdRandom.setSeed(seed);
    }


    @Override
    public Chromosome[] update(
            Chromosome father,
            Chromosome mother,
            double crossoverRate
            ){

        return update((DualPermutationChromosome) father, (DualPermutationChromosome) mother, crossoverRate);
    }


    public DualPermutationChromosome[] update(DualPermutationChromosome father,
                                              DualPermutationChromosome mother,
                                              double crossoverRate){

        int numOfContainer = father.getNumOfContainer();
        int numOfVm = father.getNumOfVm();
        DualPermutationChromosome[] children = new DualPermutationChromosome[2];

        // 为容器排列和 VM 类型实现两种类型的交叉
        // 顺序 1 专为排列表示而设计，单个点交叉是通用运算符
//        orderOneCrossover(father, mother, children, crossoverRate, numOfContainer, numOfVm);
//        singlePointCrossover(father, mother, children, crossoverRate, numOfContainer, numOfVm)

        if(StdRandom.uniform() > crossoverRate){
            children[0] = father.clone();
            children[1] = mother.clone();
            return children;
        }

        int startPoint = StdRandom.uniform(numOfContainer - 1);
        int endPoint = StdRandom.uniform(startPoint + 1, numOfContainer + 1);
        int cutPointOnVm = StdRandom.uniform(numOfVm);


        children[0] = new DualPermutationChromosome(father.cut(startPoint, endPoint, cutPointOnVm),
                                                    mother.cut(startPoint, endPoint, cutPointOnVm),
                                                    startPoint, endPoint, cutPointOnVm, numOfContainer, numOfVm);

        children[1] = new DualPermutationChromosome(mother.cut(startPoint, endPoint, cutPointOnVm),
                                                    father.cut(startPoint, endPoint, cutPointOnVm),
                                                    startPoint, endPoint, cutPointOnVm, numOfContainer, numOfVm);
        return children;
    }


}
