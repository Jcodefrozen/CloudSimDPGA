import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

import java.util.ArrayList;

/**
 * 目前，实现了突变操作并更改 VM 类型操作。
 */
public class DualPermutationMutation implements Mutation {

    private int vmTypes;
    private int numOfVm;
    private int numOfContainer;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;

    public DualPermutationMutation(int vmTypes,
                                   int numOfVm,
                                   int numOfContainer,
                                   double[] vmCpu,
                                   double[] vmMem,
                                   double[] taskCpu,
                                   double[] taskMem,
                                   double vmCpuOverheadRate,
                                   double vmMemOverhead,
                                   int seed){
        this.vmTypes = vmTypes;
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        StdRandom.setSeed(seed);
    }


    @Override
    public void update(Chromosome individual, double mutationRate){
        update((DualPermutationChromosome) individual, mutationRate);
        repairEmptyVm((DualPermutationChromosome) individual);
        repairUnAllocatedContainers((DualPermutationChromosome) individual);
    }

    public void update(DualPermutationChromosome individual, double mutationRate){
        //交换容器排列染色体的两个值
        if(StdRandom.uniform() < mutationRate){
            switchTwoContainers(individual);
//            regenerateSequence(individual);
        }

        // 改变一个虚拟机类型
        changeVMTypesMutation(individual, mutationRate);
    }

    private void regenerateSequence(DualPermutationChromosome individual){
        int[] taskSequence = generateRandomSequence();
        for(int i = 0; i < numOfContainer; i++){
            individual.containerPermutation[i] = taskSequence[i];
        }
    }

    /**
     * 生成任务随机序列
     * 基本思想是，首先初始化具有任务长度的序列，然后每次随机绘制一个，直到数组列表为空。
     * @return a random sequence
     */
    private int[] generateRandomSequence(){
        int taskCount = 0;
        int[] taskSequence = new int[numOfContainer];
        ArrayList<Integer> dummySequence = new ArrayList<Integer>();
        for(int i = 0; i < numOfContainer; i++) dummySequence.add(i);
        while(!dummySequence.isEmpty()){
            int index = StdRandom.uniform(dummySequence.size());
            taskSequence[taskCount++] = dummySequence.get(index);
            dummySequence.remove(index);
        }
        return taskSequence;
    }

    private void changeVMTypesMutation(DualPermutationChromosome individual, double mutationRate){
        for(int i = 0; i < individual.vmTypes.length; ++i){
            if(StdRandom.uniform() < mutationRate){
//                individual.vmTypes[i] = StdRandom.uniform(vmTypes);
                individual.vmTypes[i] = StdRandom.uniform(vmTypes);
            }
        }
    }


    private void switchTwoContainers(DualPermutationChromosome individual){
        int point1, point2;
        point1 = StdRandom.uniform(0, individual.containerPermutation.length - 1);
        point2 = StdRandom.uniform(point1, individual.containerPermutation.length);
        int temp = individual.containerPermutation[point1];
        individual.containerPermutation[point1] = individual.containerPermutation[point2];
        individual.containerPermutation[point2] = temp;
    }

    private void repairUnAllocatedContainers(DualPermutationChromosome individual){

        // Now, we start to repair
        while(!checkAllContainerAllocated(individual)){
            int[] vmUsed = generateMapping(individual);
            for(int i = 0; i < vmUsed.length; ++i){
                // change the first VM that is not used
                if(vmUsed[i] == 0){
                    individual.vmTypes[i] += 1;
                    if(individual.vmTypes[i] > vmTypes - 1){
                        System.out.println("Something Wrong!!!!");
                        return;
                    }
                    break;
                }
            }
        }

    }

    private boolean checkAllContainerAllocated(DualPermutationChromosome individual){
        int globalCounter = 0;
        int[] vmUsed = new int[numOfVm];

        // 循环虚拟机类型表
        for(int j = 0; j < numOfVm; ++j){
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;

            //循环容器排列表
            for(;globalCounter < numOfContainer; ++globalCounter){
                if(currentVmCpuLeft >= taskCpu[individual.containerPermutation[globalCounter]]
                        && currentVmMemLeft >= taskMem[individual.containerPermutation[globalCounter]]){
                    currentVmCpuLeft -= taskCpu[individual.containerPermutation[globalCounter]];
                    currentVmMemLeft -= taskMem[individual.containerPermutation[globalCounter]];
                    vmUsed[j] = 1;
                }else{
                    break;
                }
            }
            if(globalCounter == numOfContainer) break;
        }

        if(globalCounter < numOfContainer) return false;
        else return true;
    }

    private void repairEmptyVm(DualPermutationChromosome individual){

        // 需要做一个修复
        boolean keepRepairing = true;

        // 开始修复
        while(keepRepairing) {


            boolean foundRepair = false;

            // 检查当前 VM 的使用情况
            int[] vmUsed = generateMapping(individual);

            // 此标志检查此 VM 是否是从向后的第一个使用的 VM 检查
            boolean flag = false;

            // 我们向后检查 VM 的使用
            for (int i = numOfVm - 1; i >= 0; --i) {
                // This step will change the flag to true when the first time found a "1"
                if (vmUsed[i] == 1) flag = true;

                // 现在，发现一个无效的 vm 类型，将其调整为更大的大小
                if (flag == true && vmUsed[i] == 0) {
                    individual.vmTypes[i] += 1;
                    foundRepair = true;
                }
                if(foundRepair) keepRepairing = true;
                else keepRepairing = false;
            }
        }

    }

    private int[] generateMapping(DualPermutationChromosome individual){
        // 1 表示我们已使用此 VM，否则它为空
        int[] vmUsed = new int[numOfVm];
        int globalCounter = 0;

        for(int j = 0; j < numOfVm; ++j){
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;

            for(;globalCounter < numOfContainer; ++globalCounter){
                if(currentVmCpuLeft >= taskCpu[individual.containerPermutation[globalCounter]]
                        && currentVmMemLeft >= taskMem[individual.containerPermutation[globalCounter]]){
                    currentVmCpuLeft -= taskCpu[individual.containerPermutation[globalCounter]];
                    currentVmMemLeft -= taskMem[individual.containerPermutation[globalCounter]];
                    vmUsed[j] = 1;
                }else{
                    break;
                }
            }
            // if we have loop through all the containers, then break from the loop
            if(globalCounter == numOfContainer) break;
        }

        return vmUsed;
    }

}
