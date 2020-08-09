import algorithms.Chromosome;
import algorithms.Mutation;
import algorithms.StdRandom;

import java.util.*;

public class CecGAMutation implements Mutation {
    private int vmTypes;
    private int numOfVm;
    private int numOfContainer;
    private double vmCpuOverheadRate;
    private double vmMemOverhead;
    private double[] vmCpu;
    private double[] vmMem;
    private double[] taskCpu;
    private double[] taskMem;
    private double consolidationFactor;

    public CecGAMutation(int vmTypes,
                         int numOfVm,
                         int numOfContainer,
                         double[] vmCpu,
                         double[] vmMem,
                         double[] taskCpu,
                         double[] taskMem,
                         double vmCpuOverheadRate,
                         double vmMemOverhead,
                         double consolidationFactor,
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
        this.consolidationFactor = consolidationFactor;
        StdRandom.setSeed(seed);
    }

    public void update(Chromosome individual, double mutationRate){
        for(int i = 0; i < numOfContainer; i++){
            if(StdRandom.uniform() <= mutationRate){
                mutateContainerAllocation((CecGAChromosome) individual, i);
            }
        }

//        mutateVmAllocation((CecGAChromosome) individual);
//
//        repairIndex((CecGAChromosome) individual);

        if(!validation((CecGAChromosome) individual)){
            throw(new IllegalStateException());
        }
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
//            System.out.println("Wrong");
//            chromo.print();
            return false;
        }
        return true;

    }



    // 返回数组
    // 数组的每个条目都是vm的一种类型
    // ArrayList中的每个条目都是vm的数量
    private ArrayList<Integer>[] findExistingVms(CecGAChromosome chromo){
        ArrayList<Integer>[] existingVMTypes = new ArrayList[vmTypes];
        for(int i = 0; i < vmTypes; i++) existingVMTypes[i] = new ArrayList<Integer>();
        for(int i = 0; i < numOfContainer; i++){
            int type = chromo.individual[i * 2];
            // 如果没有机器或者当前列表没有这样的vm序号号，那么在列表中添加vm序号
            if(existingVMTypes[type].isEmpty())
                existingVMTypes[type].add(chromo.individual[i * 2 + 1]);
            if(!existingVMTypes[type].contains(chromo.individual[i * 2 + 1])){
                existingVMTypes[type].add(chromo.individual[i * 2 + 1]);
            }
        }

        return existingVMTypes;
    }


    // 最小值
    private int suitableVM(int taskNo){
        int vmType = 0;
        for(int k = 0; k < vmTypes; k++){
            if(vmCpu[k] - vmCpu[k] * vmCpuOverheadRate - taskCpu[taskNo] >= 0
                    && vmMem[k] - vmMemOverhead - taskMem[taskNo] >= 0){
                vmType = k;
                break;
            }
        }
        return vmType;
    }

    // 计算虚拟机数量
    private int countNumOfVm(CecGAChromosome chromo){
        int numOfVms = 0;
        for(int i = 0; i < numOfContainer; ++i){
            if(numOfVms < chromo.individual[i * 2 + 1]) numOfVms = chromo.individual[i * 2 + 1];
        }
        return numOfVms;
    }

    private void repairIndex(CecGAChromosome chromo){
        TreeMap<Integer, ArrayList<Integer>> vmList = new TreeMap<>();
        for(int i = 0; i < numOfContainer; ++i){
            if(vmList.containsKey(chromo.individual[i * 2 + 1])){
                ArrayList<Integer> vm = vmList.get(chromo.individual[i * 2 + 1]);
                vm.add(i);
            }else{
                ArrayList<Integer> vm = new ArrayList<>();
                vm.add(i);
                vmList.put(chromo.individual[i * 2 + 1], vm);
            }
        }
//        System.out.println("vmList.size() = " + vmList.size());

        int index = 0;
        for(Map.Entry entry:vmList.entrySet()){
            ArrayList<Integer> vm = (ArrayList<Integer>) entry.getValue();
            for(int i = 0; i < vm.size(); ++i){
                chromo.individual[vm.get(i) * 2 + 1] = index;
            }
            index += 1;
        }

    }

    private int findCloestIndex(CecGAChromosome chromo, int index){
        Integer gap = null;
        int myIndex = 0;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] > index){
                if(gap == null) {
                    gap = chromo.individual[i * 2 + 1] - index;
                    continue;
                }

                if(gap < chromo.individual[i * 2 + 1] - index) {
                    gap = chromo.individual[i * 2 + 1] - index;
                    myIndex = i;
                }
            }
        }
        return myIndex;
    }


    // 切换两组容器的vm索引和类型
    private void mutateVmAllocation(CecGAChromosome chromo){
        int numOfVms = countNumOfVm(chromo);
        int chosenVm1 = StdRandom.uniform(numOfVms);
        int typeOfVm1 = findType(chromo, chosenVm1);
        int chosenVm2 = StdRandom.uniform(numOfVms);
        int typeOfVm2 = findType(chromo, chosenVm2);
        ArrayList<Integer> vm2Containers = new ArrayList<>();

        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm2){
                vm2Containers.add(i);
            }
        }

        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm1){
                chromo.individual[i * 2] = typeOfVm2;
                chromo.individual[i * 2 + 1] = chosenVm2;
            }
        }

        for(int i = 0; i < vm2Containers.size(); ++i){
            chromo.individual[vm2Containers.get(i) * 2 + 1] = chosenVm1;
            chromo.individual[vm2Containers.get(i) * 2] = typeOfVm1;
        }
    }

    // 从现在开始，VM类型不是线性增长的，只能一个接一个地找到合适的类型。
    private int randomChooseStrongerVmType(int minimumType){
        ArrayList<Integer> suitableTypes = new ArrayList<>();
        for(int i = 0; i < vmCpu.length; i++){
            if(vmCpu[i] >= vmCpu[minimumType] && vmMem[i] >= vmMem[minimumType])
                suitableTypes.add(i);
        }

        int chosenIndex = StdRandom.uniform(suitableTypes.size());
        return suitableTypes.get(chosenIndex);
    }

    private void mutateContainerAllocation(CecGAChromosome chromo, int index){
        // 第一步，找出使用了多少vm
        int numOfVms = countNumOfVm(chromo) + 1;

        // 随机选择一个虚拟机分配, [0, numOfVms + 1)
        int chosenVm = StdRandom.uniform(numOfVms + 1);

        int typeOfChosenVm;
        // 如果我们创建一个新的VM来承载这个容器
        if(chosenVm == numOfVms){
            int minimumVm = suitableVM(index);
//            typeOfChosenVm = StdRandom.uniform(minimumVm, vmTypes);
            typeOfChosenVm = randomChooseStrongerVmType(minimumVm);
        // 否则，我们检查这个VM是否适合这个容器，如果不适合，我们就返回，不做任何更改
        } else{
            typeOfChosenVm = findType(chromo, chosenVm);
//            if(vmCpu[typeOfChosenVm] - vmCpuOverheadRate * vmCpu[typeOfChosenVm] < taskCpu[index] ||
//                    vmMem[typeOfChosenVm] - vmMemOverhead < taskMem[index]) return;
            double[] vm = buildVmTable(chromo, chosenVm, typeOfChosenVm);
            if(vm[0] < taskCpu[index] || vm[1] < taskMem[index]) return;
        }
        chromo.individual[index * 2 + 1] = chosenVm;
        chromo.individual[index * 2] = typeOfChosenVm;
//        while(checkOverloadingVm(chromo, chosenVm)){
//            fix(chromo, chosenVm);
//        }
        if(!validation(chromo)){
            repairIndex(chromo);
        }
    }

    private double[] buildVmTable(CecGAChromosome chromo, int chosenVm, int typeOfChosenVm){
        double[] vm = new double[2];
        vm[0] = vmCpu[typeOfChosenVm] - vmCpu[typeOfChosenVm] * vmCpuOverheadRate;
        vm[1] = vmMem[typeOfChosenVm] - vmMemOverhead;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm){
                vm[0] -= taskCpu[i];
                vm[1] -= taskMem[i];
            }
        }
        return vm;
    }

//    private void fix(CecGAChromosome chromo, int chosenVm){
//        for(int i = 0; i < numOfContainer; ++i){
//            // Start from the first one, move it to other feasible VM
//            if(chromo.individual[i * 2 + 1] == chosenVm){
//
//            }
//        }
//    }

    private boolean checkOverloadingVm(CecGAChromosome chromo, int chosenVm){
        int type = findType(chromo, chosenVm);
        double[] vm = new double[2];
        vm[0] = vmCpu[type] - vmCpu[type] * vmCpuOverheadRate;
        vm[1] = vmMem[type] - vmMemOverhead;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm){
                vm[0] -= taskCpu[i];
                vm[1] -= taskMem[i];
                if(vm[0] < 0 || vm[1] < 0)  return true;
            }
        }

        return false;
    }


    private Integer findType(CecGAChromosome chromo, int chosenVm){
        Integer vmType = null;
        for(int i = 0; i < numOfContainer; ++i){
            if(chromo.individual[i * 2 + 1] == chosenVm){
                vmType = chromo.individual[i * 2];
                break;
            }
        }
        return vmType;
    }

}
