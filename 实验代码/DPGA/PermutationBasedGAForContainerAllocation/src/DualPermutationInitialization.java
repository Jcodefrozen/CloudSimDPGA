import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.Random;

public class DualPermutationInitialization implements InitPop {

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int numOfVm;
    private int vmType;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;

    // 构造函数
    public DualPermutationInitialization(int numOfContainer,
                                         int numOfVm,
                                         int vmType,
                                         int seed,
                                         double[] vmCpu,
                                         double[] vmMem,
                                         double[] taskCpu,
                                         double[] taskMem,
                                         double vmCpuOverheadRate,
                                         double vmMemOverhead
                                         ){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmType = vmType;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

        StdRandom.setSeed(seed);
    }

    // 生成种群
    public DualPermutationChromosome[] init(int popSize, int maxVar, double lbound, double ubound){
        DualPermutationChromosome[] pop = new DualPermutationChromosome[popSize];
        for(int i = 0; i < popSize; i++){
            DualPermutationChromosome temp = generateChromosome();
            repairEmptyVm(temp);
            repairUnAllocatedContainers(temp);
            pop[i] = temp;
        }
        return pop;
    }

    // 生成个体
    private DualPermutationChromosome generateChromosome(){
        DualPermutationChromosome individual = new DualPermutationChromosome(numOfContainer, numOfVm);
        individual.containerPermutation = generateRandomSequence();
        individual.vmTypes = generateRandomTypes();
        return individual;
    }


    private int[] generateRandomTypes(){
        int count = 0;
        int[] vmTypes = new int[numOfVm];
        while(count != numOfVm){
            vmTypes[count] = StdRandom.uniform(vmType);
            count++;
        }
        return vmTypes;
    }

    /**
     * 生成任务随机序列
     * 基本思想是，首先初始化具有任务长度的序列
     * 然后，每次随机绘制一个，直到数组列表为空。
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

	private void repairUnAllocatedContainers(DualPermutationChromosome individual){
        while(!checkAllContainerAllocated(individual)){
            int[] vmUsed = generateMapping(individual);
            for(int i = 0; i < vmUsed.length; ++i){
                // 更改未使用的第一个 VM
                if(vmUsed[i] == 0){
                    individual.vmTypes[i] += 1;
                    if(individual.vmTypes[i] > vmType - 1){
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

        // 循环浏览 VM 类型数组
        for(int j = 0; j < numOfVm; ++j){
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;

            // 循环通过容器排列数组
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
            // 循环通过容器排列数组，如果我们有循环通过所有容器，然后从循环中断
            if(globalCounter == numOfContainer) break;
        }

        if(globalCounter < numOfContainer) return false;
        else return true;
    }

	private void repairEmptyVm(DualPermutationChromosome individual){

	    // 需要做一个修复
        boolean keepRepairing = true;


        while(keepRepairing) {

            // 在这一轮中发现任何修复
            boolean foundRepair = false;

            // 检查当前 VM 的使用情况
            int[] vmUsed = generateMapping(individual);

            // 此标志检查此 VM 是否是从向后的第一个使用的 VM 检查
            boolean flag = false;

            //从向后检查 VM 的使用
            for (int i = numOfVm - 1; i >= 0; --i) {
                // 当第一次发现"1"时，此步骤将标志更改为 true
                if (vmUsed[i] == 1) flag = true;

                // 发现一个无效的 vm 类型，将其调整为更大的序列
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

        // 循环浏览 VM 类型数组
        for(int j = 0; j < numOfVm; ++j){
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;

            //循环通过容器排列数组
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
            //如果循环通过所有的容器，然后打破循环
            if(globalCounter == numOfContainer) break;
        }

        return vmUsed;
    }




}
