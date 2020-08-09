import algorithms.Chromosome;
import algorithms.Gene;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DualPermutationChromosome extends Chromosome{
    public int[] containerPermutation;
    public int[] vmTypes;
    private int numOfContainer;
    private int numOfVm;

    // 评估阶段后将填充以下字段。
    // pmList 包括 PM，
    // 每个 PM 都包含 VM 的列表，
    // 每个 VM 都表示为 int[]，具有两个属性，
    // [0] VM索引, [1] VM类型
    private ArrayList<ArrayList<int[]>> pmList;


    //pmStatusList 包括每个物理机的状态，
    // 每个 PM 状态表示为double[]，具有三个属性
    // [0] CPU利用率, [1] 内存利用率, [2] 能耗
    private ArrayList<double[]> pmStatusList;


    // vmList 包括 VM
    //每个 VM 都有一个容器数组，
    // 数组的每个条目都是容器的索引
    private ArrayList<int[]> vmList;

    // vmStatusList 包括 VM 的状态
    // 每个 VM 状态都表示为doucle[]，具有两个属性
    //[0] CPU利用率, [1] Mem利用率
    private ArrayList<double[]> vmStatusList;

    // 适应度值
    private double fitness;

    /**
     * 构造函数 1
     */
    public DualPermutationChromosome(int numOfContainer, int numOfVm){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;

        containerPermutation = new int[numOfContainer];
        vmTypes = new int[numOfVm];
    }

    /**
     * 构造函数 2
     * 构造函数用于交叉生成子代，在这种情况下，它设计为顺序 1 类型的交叉
     */
    public DualPermutationChromosome(DualPermutationGene firstPart,
                                     DualPermutationGene secondPart,
                                     int startPointOnContainer,
                                     int endPointOnContainer,
                                     int cutPointOnVm,
                                     int numOfContainer,
                                     int numOfVm){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;

        containerPermutation = generateContainers(firstPart, secondPart,
                                                    startPointOnContainer,
                                                    endPointOnContainer);

        vmTypes = generateVmTypes(firstPart, secondPart, cutPointOnVm, numOfVm);

    }

    public void setVmList(ArrayList<int[]> vmList){
        this.vmList = vmList;
    }
    public void setVmStatusList(ArrayList<double[]> vmStatusList){
        this.vmStatusList = vmStatusList;
    }
    public void setPmList(ArrayList<ArrayList<int[]>> pmList){
        this.pmList = pmList;
    }
    public void setPmStatusList(ArrayList<double[]> pmStatusList){
        this.pmStatusList = pmStatusList;
    }
    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public int getNoOfUsedVm(){
        if(vmList == null || vmList.isEmpty()){
            return 0;
        } else {
            return vmList.size();
        }
    }

    public int getNumOfPm(){
        if(pmList == null || pmList.isEmpty()){
            return 0;
        } else {
            return pmList.size();
        }
    }

    public double averagePmCpuUtil(double pmCpu){
        double totalCpuUtil = 0;
        for(double[] pmStatus:pmStatusList){
            totalCpuUtil += (pmStatus[0] / pmCpu);
        }

        return totalCpuUtil / pmStatusList.size();
    }

    public double averagePmMemUtil(double pmMem){
        double totalMemUtil = 0;
        for(double[] pmStatus:pmStatusList){
            totalMemUtil += (pmStatus[1] / pmMem);
        }

        return totalMemUtil / pmStatusList.size();
    }


    @Override
    public DualPermutationGene cut(int cutPoint, int geneIndicator) {
        return null;
    }

    public int getNumOfContainer(){
        return numOfContainer;
    }

    public int getNumOfVm(){
        return numOfVm;
    }

    @Override
    public int size(){
        return 0;
    }


    // 在这种表示中，整个染色体作为基因。我们不能将它们分成更小的部分。
    public DualPermutationGene cut(int startPointOnContainer, int endPointOnContainer, int cutPointOnVm){
        DualPermutationGene part = new DualPermutationGene(numOfContainer, numOfVm);

        // 将当前值复制到基因中
        System.arraycopy(containerPermutation, 0, part.containerPermutation, 0, numOfContainer);
        System.arraycopy(vmTypes, 0, part.vmTypes, 0, numOfVm);
        return part;
    }

    @Override
    public DualPermutationChromosome clone(){
        DualPermutationChromosome ind = new DualPermutationChromosome(numOfContainer, numOfVm);
        System.arraycopy(containerPermutation, 0, ind.containerPermutation, 0, numOfContainer);
        System.arraycopy(vmTypes, 0, ind.vmTypes, 0, numOfVm);
        return ind;
    }

    @Override
    public void print(){
        System.out.println("container Permutation: ");
        for(int i = 0; i < numOfContainer; ++i){
            System.out.print(containerPermutation[i] + " ");
        }
        System.out.println();
        System.out.println("vm Types: ");
        for(int i = 0; i < numOfVm; ++i){
            System.out.print(vmTypes[i] + " ");
        }
        System.out.println();
        System.out.println("numOfPm = " + getNumOfPm());
        System.out.println("ActualUsedVm = " + getNoOfUsedVm());
        System.out.println("averagePmCpuUtil = " + averagePmCpuUtil(3300));
        System.out.println("averagePmMemUtil = " + averagePmMemUtil(4000));
    }

    @Override
    public boolean equals(Chromosome o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DualPermutationChromosome that = (DualPermutationChromosome) o;
        return numOfContainer == that.numOfContainer &&
                numOfVm == that.numOfVm &&
                Arrays.equals(containerPermutation, that.containerPermutation) &&
                Arrays.equals(vmTypes, that.vmTypes);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(numOfContainer, numOfVm);
        result = 31 * result + Arrays.hashCode(containerPermutation);
        result = 31 * result + Arrays.hashCode(vmTypes);
        return result;
    }


    private int[] generateVmTypes(DualPermutationGene firstPart,
                                  DualPermutationGene secondPart,
                                  int cutPointOnVm, int numOfVm){
        vmTypes = new int[numOfVm];
        for(int i = 0; i < cutPointOnVm; i++){
            vmTypes[i] = firstPart.vmTypes[i];
        }
        for(int i = cutPointOnVm; i < firstPart.getNumOfVms(); i++){
            vmTypes[i] = secondPart.vmTypes[i];
        }
        return vmTypes;
    }

    private int[] generateContainers(DualPermutationGene firstPart,
                                     DualPermutationGene secondPart,
                                     int startPoint, int endPoint){

        containerPermutation = new int[numOfContainer];
        // 条件1 startPoint == 0 && endPoint == numOfContainer, 整个染色体被复制
        if(startPoint == 0 && endPoint == numOfContainer){
            System.arraycopy(firstPart.containerPermutation, 0, containerPermutation, 0, numOfContainer);
            return containerPermutation;
        }


        // 条件2 -- 从开始切割
        if(startPoint == 0 && endPoint != numOfContainer){
            int countIndex = endPoint;
            int[] tail = new int[numOfContainer - endPoint];
            int[] head = new int[endPoint];
            System.arraycopy(secondPart.containerPermutation, endPoint, tail,
                            0, numOfContainer - endPoint);
            System.arraycopy(secondPart.containerPermutation, 0, head,
                            0, endPoint);

            // 复制到子代
            System.arraycopy(firstPart.containerPermutation, startPoint,
                    containerPermutation, startPoint, endPoint - startPoint);

            // 检查尾部
            for (int i = 0; i < tail.length; ++i) {
                if (!valueExists(tail[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = tail[i];
                    countIndex++;
                }
            }

            // 检查头部
            for(int i = 0; countIndex < numOfContainer; ++i){
                if (!valueExists(head[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = head[i];
                    countIndex++;
                }
            }

            return containerPermutation;
        }

        // 条件2 -- 从中间切割到末端
        if(startPoint != 0 && endPoint == numOfContainer){
            int countIndex = 0;
            int[] head = new int[startPoint];
            int[] tail = new int[numOfContainer - startPoint];

            System.arraycopy(secondPart.containerPermutation, startPoint, tail,
                    0, numOfContainer - startPoint);
            System.arraycopy(secondPart.containerPermutation, 0, head,
                    0, startPoint);

            // 复制到子代
            System.arraycopy(firstPart.containerPermutation, startPoint,
                    containerPermutation, startPoint, endPoint - startPoint);



            // 检查头部
            for(int i = 0; i < head.length; ++i){
                if (!valueExists(head[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = head[i];
                    countIndex++;
                }
            }

            // 检查尾部
            for (int i = 0; countIndex < startPoint; ++i) {
                if (!valueExists(tail[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = tail[i];
                    countIndex++;
                }
            }

            return containerPermutation;
        }


        // 条件3
        if(startPoint != 0 && endPoint != numOfContainer){
            int countIndex = endPoint;
            int headPointer = 0;
            int[] head = new int[endPoint];
//            int[] middle = new int[endPoint - startPoint];
            int[] tail = new int[numOfContainer - endPoint];


            // 复制到子代
            System.arraycopy(firstPart.containerPermutation, startPoint,
                    containerPermutation, startPoint, endPoint - startPoint);

            // 复制3部分
            System.arraycopy(secondPart.containerPermutation, endPoint, tail,
                    0, numOfContainer - endPoint);
//            System.arraycopy(secondPart.containerPermutation, startPoint, middle,
//                    0, endPoint - startPoint);
            System.arraycopy(secondPart.containerPermutation, 0, head,
                    0, endPoint);

            // 检查尾部
            for (int i = 0; i < tail.length; ++i) {
                if (!valueExists(tail[i], firstPart.containerPermutation, startPoint, endPoint)) {
                    containerPermutation[countIndex] = tail[i];
                    countIndex++;
                }
            }

            // 到了终点
            if(countIndex == numOfContainer){
                countIndex = 0;

                // 检查头部
                for(; countIndex < startPoint; ++headPointer){
                    if (!valueExists(head[headPointer], firstPart.containerPermutation, startPoint, endPoint)) {
                        containerPermutation[countIndex] = head[headPointer];
                        countIndex++;
                    }
                }


            } else {
                // 检查head
                for(; countIndex < numOfContainer; ++headPointer){
                    if (!valueExists(head[headPointer], firstPart.containerPermutation, startPoint, endPoint)) {
                        containerPermutation[countIndex] = head[headPointer];
                        countIndex++;
                    }
                }

                // 到达尾端
                countIndex = 0;

                // 检查头部
                for(; countIndex < startPoint; ++headPointer){
                    if (!valueExists(head[headPointer], firstPart.containerPermutation, startPoint, endPoint)) {
                        containerPermutation[countIndex] = head[headPointer];
                        countIndex++;
                    }
                }
            }

        } // End third condition

        return containerPermutation;
    }
    // 值范围 [startPoint, endPoint)
    private boolean valueExists(int value, int[] myArray, int startPoint, int endPoint){
        for(int i = startPoint; i < endPoint; ++i){
            if(value == myArray[i]) return true;
        }
        return false;
    }

}
