import algorithms.Chromosome;
import algorithms.UnNormalizedFit;

import java.util.ArrayList;

public class EnergyFitness extends UnNormalizedFit{

    private static int numOfContainer;
    private static int numOfVm;
    private static double k;
    private static double pmCpu;
    private static double pmMem;
    private static double pmMaxEnergy;
    private static double[] vmCpu;
    private static double[] vmMem;
    private static double[] taskCpu;
    private static double[] taskMem;
    private static double vmCpuOverheadRate;
    private static double vmMemOverhead;

    // 构造函数 1
    public EnergyFitness(int numOfContainer,
                         int numOfVm,
                         double k,
                         double pmCpu,
                         double pmMem,
                         double pmEnergy,
                         double vmCpuOverheadRate,
                         double vmMemOverhead,
                         double[] vmCpu,
                         double[] vmMem,
                         double[] taskCpu,
                         double[] taskMem){
        super(null);

        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.k = k;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
        this.pmMaxEnergy = pmEnergy;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;

    }


    public EnergyFitness(Chromosome individual){
        super(individual);
    }
    @Override
    public Object call() throws Exception {
        double[] fit = new double[2];

        Double energyConsumption = 0.0;
        // 虚拟机表
        ArrayList<ArrayList<Integer>> vmList = new ArrayList<>();
        // 物理机表
        ArrayList<ArrayList<ArrayList<Integer>>> pmList = new ArrayList<>();
        // 虚拟机是否被使用 1或0
        // 作用是填充 vmList 和 vmStatusList
        int[] usedVm = generateMapping((DualPermutationChromosome) individual, vmList);

        // 计算虚拟机
        int actualUsedVm = countUsedVm(usedVm);

        // 将使用的 VM 类型映射到实际使用VM
        int[] actualUsageVm = getRealUsageOfVms((DualPermutationChromosome) individual, usedVm, actualUsedVm);

        // 现在，将 VM 分配给 PM 以构建完整的解决方案
        allocateVMs(actualUsageVm, vmList, pmList, actualUsedVm);

        // 最后，我们可以根据pmList的完整解决方案计算能量
        energyConsumption = energy(pmList, actualUsageVm);
//        System.out.println("energy Consumption = " + energyConsumption);

        ((DualPermutationChromosome) individual).setFitness(energyConsumption);
        fit[0] = energyConsumption;
        fit[1] = 0;

        return fit;
    }

    // 将 ArrayList 转换为数组，并分配给个体
    private void setVmListToIndividual(DualPermutationChromosome individual, ArrayList<ArrayList<Integer>> vmList){
        ArrayList<int[]> subVmList = new ArrayList<>();
        for(int i = 0; i < vmList.size(); i++){
            ArrayList<Integer> vm = vmList.get(i);
            int[] vmContainer = new int[vm.size()];
            for(int j = 0; j < vm.size(); j++){
                vmContainer[j] = vm.get(j);
            }
            subVmList.add(vmContainer);
        }

        individual.setVmList(subVmList);
    }

    private int countUsedVm(int[] usedVm){
        int numOfUsedVm = 0;
        for(int i = 0; i < usedVm.length; i++){
            if(usedVm[i] == 1)
                numOfUsedVm++;
        }
//        System.out.println("used VM number: " + numOfUsedVm);
        return numOfUsedVm;
    }


    /**
     * 计算所有物理机的能耗
     * @param pmList
     * @return
     */
    private double energy(ArrayList<ArrayList<ArrayList<Integer>>> pmList, int[] actualUsedVm){
        double totalEnergy = 0;
        int globalVmCounter = 0;
        ArrayList<double[]> pmStatusList = new ArrayList<>();

        for(int pmCount = 0; pmCount < pmList.size(); ++pmCount){
            ArrayList<ArrayList<Integer>> pm = pmList.get(pmCount);
            double[] pmStatus = new double[3];
            double pmEnergy = 0;
            double pmCpuUsage = 0;
            double pmMemUsage = 0;
            double pmCpuUtil = 0;
            for(int vmCount = 0; vmCount < pm.size(); ++vmCount, ++globalVmCounter){
                ArrayList<Integer> vm = pm.get(vmCount);
                double vmCpuUsage = vmCpuOverheadRate * vmCpu[actualUsedVm[globalVmCounter]];
                double vmMemUsage = vmMemOverhead;
                for(int containerCount = 0; containerCount < vm.size(); ++containerCount){
                    vmCpuUsage += taskCpu[vm.get(containerCount)];
                    vmMemUsage += taskMem[vm.get(containerCount)];
                }
                pmCpuUsage += vmCpuUsage;
                pmMemUsage += vmMemUsage;
            }

            pmCpuUtil = pmCpuUsage / pmCpu;
//            //---------------------Debug------------------------------
//            System.out.println("pmCpuUtil = " + pmCpuUtil);
//            System.out.println("pmMaxEnergy = " + pmMaxEnergy);
//            System.out.println("k = " + k);
//            //---------------------Debug------------------------------

            pmEnergy = k * pmMaxEnergy + (1 - k) * pmMaxEnergy * pmCpuUtil;
//            System.out.println("pm " + pmCount + ", energy = " + pmEnergy);
//            //---------------------Debug------------------------------
//            System.out.println("pmEnergy = " + pmEnergy);
//            //---------------------Debug------------------------------
            pmStatus[0] = pmCpuUsage;
            pmStatus[1] = pmMemUsage;
            pmStatus[2] = pmEnergy;
            pmStatusList.add(pmStatus);
            totalEnergy += pmEnergy;
        }
        ((DualPermutationChromosome)individual).setPmStatusList(pmStatusList);
//        System.out.println("pmSize = " + pmList.size() + ", totalEnergy = " + totalEnergy);
        return totalEnergy;

    }

    /**
     * Use FF to allocate VMs to PMs
     * @param actualUsageVm
     * @param vmList
     * @param pmList
     */
    private void allocateVMs(int[] actualUsageVm, ArrayList<ArrayList<Integer>> vmList,
                             ArrayList<ArrayList<ArrayList<Integer>>> pmList, int actualUsedVm){


        int globalVmCounter = 0;
        boolean noVmleft = false;

        // pmVmList包括了物理机
        // 每个物理机有一个虚拟机列表
        // 每个虚拟机用int[]表示
        // [0] 虚拟机索引, [1]虚拟机类型
        ArrayList<ArrayList<int[]>> pmVmList = new ArrayList<>();

//        System.out.println("actualUsageVm = ");
//        for(int i = 0; i < actualUsageVm.length; i++){
//            System.out.print(actualUsageVm[i] + " ");
//        }
//        System.out.println();
//        System.out.println("actualUsedVm : " + actualUsedVm);

        // 当还剩下 VM 时
        while(!noVmleft){
            // create a new PM
            double currentPmCpu = pmCpu;
            double currentPmMem = pmMem;

            //创建物理机
            ArrayList<ArrayList<Integer>> pm = new ArrayList<>();

            // 一个虚拟列表
            ArrayList<int[]> vms = new ArrayList<>();


            // 分配虚拟机
            for(;globalVmCounter < actualUsedVm; globalVmCounter++){

                // 如果我们可以将此 VM 分配给此 PM，则将 vmList 添加到 PM
                if(currentPmCpu >= vmCpu[actualUsageVm[globalVmCounter]] &&
                   currentPmMem >= vmMem[actualUsageVm[globalVmCounter]]){
                    currentPmCpu -= vmCpu[actualUsageVm[globalVmCounter]];
                    currentPmMem -= vmMem[actualUsageVm[globalVmCounter]];
                    pm.add(vmList.get(globalVmCounter));

                    int[] vm = new int[2];
                    vm[0] = globalVmCounter;
                    vm[1] = actualUsageVm[globalVmCounter];
                    vms.add(vm);
                // 否则，此 PM 已填充、中断并启动新 PM
                } else{
                    break;
                }
            }

            pmVmList.add(vms);
            pmList.add(pm);

            // 是否将所有VM分配给了PM？
            if(globalVmCounter == actualUsedVm) noVmleft = true;
        } // end of While

        ((DualPermutationChromosome)individual).setPmList(pmVmList);


    }

    /**
     * 将indidual.vmTypes染色体与usedVM作“和”操作，找出虚拟机的真实的使用情况
     * @param individual
     * @param usedVm
     * @return
     */
    private int[] getRealUsageOfVms(DualPermutationChromosome individual, int[] usedVm, int actualUsedVm){
        int[] realUsageOfVms = new int[actualUsedVm];


        for(int i = 0; i < actualUsedVm; ++i) {
            realUsageOfVms[i] = individual.vmTypes[i];
        }
        return realUsageOfVms;
    }

    private int[] generateMapping(DualPermutationChromosome individual, ArrayList<ArrayList<Integer>> vmList){
        // 1表示已经使用了虚拟机否则为空
        int[] vmUsed = new int[numOfVm];
        int globalCounter = 0;
        ArrayList<double[]> vmStatusList = new ArrayList<>();

        // 循环虚拟机类型表
        for(int j = 0; j < numOfVm; ++j){
            ArrayList<Integer> containerList = new ArrayList<>();
            double currentVmCpuLeft = vmCpu[individual.vmTypes[j]] - vmCpu[individual.vmTypes[j]] * vmCpuOverheadRate;
            double currentVmMemLeft = vmMem[individual.vmTypes[j]] - vmMemOverhead;
            double[] vmStatus = new double[2];

            // 循环通过容器排列数组。
            // 添加在此 VM 上分配的所有容器
            for(;globalCounter < numOfContainer; ++globalCounter){
                // first, we find out the real index of the container
                int indexOfContainer = individual.containerPermutation[globalCounter];

                if(currentVmCpuLeft >= taskCpu[indexOfContainer]
                        && currentVmMemLeft >= taskMem[indexOfContainer]){
                    currentVmCpuLeft -= taskCpu[indexOfContainer];
                    currentVmMemLeft -= taskMem[indexOfContainer];

                    // 将容器的值添加到容器列表中
                    containerList.add(indexOfContainer);
                    vmUsed[j] = 1;
                // 否则此 vm 已填充、中断并启动新 VM
                }else{
                    vmStatus[0] = vmCpu[individual.vmTypes[j]] - currentVmCpuLeft;
                    vmStatus[1] = vmMem[individual.vmTypes[j]] - currentVmMemLeft;
                    vmStatusList.add(vmStatus);
                    break;
                }
            }

            // 将此 VM 添加到 vmList
            vmList.add(containerList);
            if(globalCounter == numOfContainer) break;
        }

        setVmListToIndividual(individual, vmList);
        individual.setVmStatusList(vmStatusList);

        return vmUsed;
    }
}
