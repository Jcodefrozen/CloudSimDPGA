import algorithms.Gene;
import commonRepresentation.IntGene;
import commonRepresentation.IntValueChromosome;

import java.util.ArrayList;
import java.util.HashMap;

public class CecGAChromosome extends IntValueChromosome{
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


    // Fitness value
    private double fitness;

    public CecGAChromosome(Gene firstPart, Gene secondPart){
        super(firstPart, secondPart);
    }

    public CecGAChromosome(int size){
        super(size);
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


    public ArrayList<double[]> getPmStatusList() {
        return pmStatusList;
    }

    public int getNoOfUsedVm(){
        if(vmList == null || vmList.isEmpty()){
            return 0;
        } else {
            return vmList.size();
        }
    }

    public void printPmList(){
        for(int i = 0; i < pmList.size(); i++){
            ArrayList<int[]> pm = pmList.get(i);
            System.out.print("PM " + i + ": ");
            for(int j = 0; j < pm.size(); j++){
                System.out.print("index = " + pm.get(j)[0] + " : type = " + pm.get(j)[1] + ", ");
            }
            System.out.println();
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

    public void print() {
        for(int i = 0; i < size(); i++){
            System.out.print(individual[i] + " ");
            if(((i + 1) % 2 == 0) && ((i + 1) != 0)) System.out.print(" | ");
        }
        System.out.println();
        System.out.println("numOfPm = " + getNumOfPm());
        System.out.println("ActualUsedVm = " + getNoOfUsedVm());
        System.out.println("averagePmCpuUtil = " + averagePmCpuUtil(3300));
        System.out.println("averagePmMemUtil = " + averagePmMemUtil(4000));
//        printPmList();
    }

    public CecGAChromosome clone(){
        CecGAChromosome copy = new CecGAChromosome(size());
        for(int i = 0; i < size(); i++){
            copy.individual[i] = individual[i];
        }
        return copy;
    }

    public IntGene cut(int cutPoint, int geneIndicator){
        IntGene part;
        if(geneIndicator == 0){
            part = new IntGene(cutPoint + 1);
            for(int i = 0; i < cutPoint + 1; i++){
                part.gene[i] = individual[i];
            }
        } else{
            part = new IntGene(size() - (cutPoint + 1));
            for(int i = cutPoint + 1, j = 0; i < size(); i++, j++){
                part.gene[j] = individual[i];
            }
        }
        return part;
    }


}
