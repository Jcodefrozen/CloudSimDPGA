import algorithms.InitPop;
import algorithms.StdRandom;

import java.util.ArrayList;
import java.util.HashSet;

public class CecGAInitialization implements InitPop{

    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private int numOfContainer;
    private int numOfVm;
    private int vmTypes;
    private double[] vmMem;
    private double[] vmCpu;
    private double[] taskCpu;
    private double[] taskMem;
    private double consolidationFactor;



    public CecGAInitialization(int numOfContainer,
                               int numOfVm,
                               int vmTypes,
                               int seed,
                               double[] vmCpu,
                               double[] vmMem,
                               double[] taskCpu,
                               double[] taskMem,
                               double vmCpuOverheadRate,
                               double vmMemOverhead,
                               double consolidationFactor
                                ){
        this.numOfContainer = numOfContainer;
        this.numOfVm = numOfVm;
        this.vmTypes = vmTypes;
        this.vmCpu = vmCpu;
        this.vmMem = vmMem;
        this.taskCpu = taskCpu;
        this.taskMem = taskMem;
        this.vmCpuOverheadRate = vmCpuOverheadRate;
        this.vmMemOverhead = vmMemOverhead;
        this.consolidationFactor = consolidationFactor;

        StdRandom.setSeed(seed);
    }


    @Override
    public CecGAChromosome[] init(
            int popSize,
            int maxVar,
            double lbound,
            double ubound
    ) {
        CecGAChromosome[] popVar = new CecGAChromosome[popSize];
        // 初始化种群
        for(int i = 0; i < popSize; i++){
            popVar[i] = generateChromosome();
        }
        return popVar;
    }

    // 返回合适VM的索引，如果没有合适的VM，返回null。
    private Integer firstFit(ArrayList<double[]> vmList, double taskCpu, double taskMem){
        Integer suitableIndex = null;
        if(vmList.isEmpty()) return suitableIndex;
        for(int i = 0; i < vmList.size(); ++i){
            double[] vm = vmList.get(i);
            if(vm[0] >= taskCpu && vm[1] >= taskMem) return i;
        }
        return suitableIndex;
    }


    // 找到可以承载此容器的最小vm类型，返回vmTypes的索引
    private int minimumVm(double taskCpu, double taskMem){
        int index = 0;
        for(; index < vmTypes; ++index){
            if(vmCpu[index] - vmCpu[index] * vmCpuOverheadRate >= taskCpu &&
               vmMem[index] - vmMemOverhead >= taskMem) return index;
        }
        return index;
    }


    // 生成个体
    private CecGAChromosome generateChromosome(){
        CecGAChromosome chromo = new CecGAChromosome(numOfContainer * 2);

        // double[0]: leftCpu, double[1]: leftMem, double[2]: vm type
        ArrayList<double[]> vmList = new ArrayList<>();

        for(int i = 0; i < numOfContainer; ++i){
            Integer suitableVm = firstFit(vmList, taskCpu[i], taskMem[i]);

            if(suitableVm != null){
                double[] vm = vmList.get(suitableVm);
                vm[0] -= taskCpu[i];
                vm[1] -= taskMem[i];
                chromo.individual[i * 2] = (int) vm[2];
                chromo.individual[i * 2 + 1] = suitableVm;
            // 否则，没有合适的VM存在，我们创建一个新的VM
            // 首先，我们找到一个最小的VM类型来承载这个容器
            // 其次，我们随机生成一个更强的类型来承载这个容器
            } else{
                suitableVm = minimumVm(taskCpu[i], taskMem[i]);
//                int generateVmType = StdRandom.uniform(suitableVm, vmTypes);
//                int generateVmType = StdRandom.uniform(suitableVm, vmTypes);
                int generateVmType = randomChooseStrongerVmType(suitableVm);

                double[] vm = new double[3];
                vm[0] = vmCpu[generateVmType] - vmCpu[generateVmType] * vmCpuOverheadRate - taskCpu[i];
                vm[1] = vmMem[generateVmType] - vmMemOverhead - taskMem[i];
                vm[2] = (double) generateVmType;
                vmList.add(vm);
                chromo.individual[i * 2] = (int) vm[2];
                chromo.individual[i * 2 + 1] = vmList.size() - 1;
            }
        }

        if(!validation(chromo)){
            throw(new IllegalStateException());
        }
        return chromo;
    }

    // 从现在开始，VM类型不是线性增长的，我们只能一个接一个地找到合适的类型。
    private int randomChooseStrongerVmType(int minimumType){
        ArrayList<Integer> suitableTypes = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            if(vmCpu[i] >= vmCpu[minimumType] && vmMem[i] >= vmMem[minimumType])
                suitableTypes.add(i);
        }

        int chosenIndex = StdRandom.uniform(suitableTypes.size());
        return suitableTypes.get(chosenIndex);
    }


    private boolean validation(CecGAChromosome chromo){
        HashSet<Integer> vmSet = new HashSet<Integer>();
        int topNum = 0;
        for(int i = 0; i < numOfContainer; i++){
            vmSet.add(chromo.individual[i * 2 + 1]);
            if(topNum < chromo.individual[i * 2 + 1])
                topNum = chromo.individual[i * 2 + 1];
        }
        if(vmSet.size() != topNum + 1){
//            chromo.print();
            return false;
        }
        return true;

    }

}
