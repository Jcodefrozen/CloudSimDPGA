import java.util.ArrayList;

public class PostProcessingUnit {
    private ArrayList<DualPermutationChromosome> bestIndividualList;
    private int run;
    private int generation;
    private double pmCpu;
    private double pmMem;

    public PostProcessingUnit(ArrayList<DualPermutationChromosome> bestIndividualList,
                              int generation, int run,
                              double pmCpu, double pmMem){
        this.bestIndividualList = bestIndividualList;
        this.generation = generation;
        this.run = run;
        this.pmCpu = pmCpu;
        this.pmMem = pmMem;
    }

    public double[] energy(){
        double[] energyConsumption = new double[run];
        for(int i = 0; i < run; i++){
            energyConsumption[i] = bestIndividualList.get((generation - 1) * (i + 1)).getFitness();
        }
        return energyConsumption;
    }


    //N 次运行中的平均 PM 能耗
    public double averageEnergy(){
        double[] energy = new double[run];
        for(int i = 0; i < run; i++){
            energy[i] = bestIndividualList.get((generation - 1) * (i + 1)).getFitness();
        }

        double sumEnergy = 0;
        for(int i = 0; i < run; i++){
            sumEnergy += energy[i];
        }

        return sumEnergy / run;
    }


    // 第N次SD PM 能耗
    public double sdEnergy(){
        double aveEnergy = averageEnergy();
        double sd = 0.0;
        double[] energy = new double[run];
        double sdSum = 0.0;
        for(int i = 0; i < run; i++){
            energy[i] = bestIndividualList.get((generation - 1) * (i + 1)).getFitness();
        }

        for(int i = 0; i < run; i++){
            sdSum += energy[i] * energy[i];
        }

        sd = Math.sqrt(sdSum / run - aveEnergy * aveEnergy);

        return sd;
    }



    // N 次运行中的平均物理机CPU 和内存利用率
    public double[] averageUtil(){
        double[] util = new double[2];
        double[] cpuUtil = new double[run];
        double[] memUtil = new double[run];

        for(int i = 0; i < run; i++){
            cpuUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmCpuUtil(pmCpu);
            memUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmMemUtil(pmMem);
        }

        double totalCpuUtil = 0;
        double totalMemUtil = 0;
        for(int i = 0; i < run; i++){
            totalCpuUtil += cpuUtil[i];
            totalMemUtil += memUtil[i];
        }

        util[0] = totalCpuUtil / run;
        util[1] = totalMemUtil / run;
        return util;
    }

    // PM CPU 的 SD 和 N 运行中的内存利用率
    public double[] sdUtil(){
        double[] sdUtil = new double[2];

        double[] aveUtil = averageUtil();
        double aveCpuUtil = aveUtil[0];
        double aveMemUtil = aveUtil[1];


        double[] cpuUtil = new double[run];
        double[] memUtil = new double[run];

        double sumCpuUtil = 0.0;
        double sumMemUtil = 0.0;

        for(int i = 0; i < run; i++){
            cpuUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmCpuUtil(pmCpu);
            memUtil[i] = bestIndividualList.get((generation - 1) * (i + 1)).averagePmMemUtil(pmMem);
        }

        for(int i = 0; i < run; i++){
            sumCpuUtil += cpuUtil[i] * cpuUtil[i];
            sumMemUtil += memUtil[i] * memUtil[i];
        }

        sdUtil[0] = Math.sqrt((sumCpuUtil / run) - aveCpuUtil * aveCpuUtil);
        sdUtil[1] = Math.sqrt((sumMemUtil / run) - aveMemUtil * aveMemUtil);

        return sdUtil;
    }



    // 收敛曲线
    //  计算所有 N 运行中每一代的平均适应度值
    public double[] convergenceCurve(){
        ArrayList<double[]> fitnessList = new ArrayList<>();
        for(int i = 0; i < generation; i++){
            double[] fitness = new double[run];
            for(int j = 0; j < run; j++){
                fitness[j] = bestIndividualList.get(j * generation + i).getFitness();
            }
            fitnessList.add(fitness);
        }

        double[] aveFitness = new double[generation];
        for(int i = 0; i < generation; i++){
            aveFitness[i] = sumArray(fitnessList.get(i)) / run;
        }
        return aveFitness;
    }

    // 虚拟机数量
    public int averageNoOfVm(){
        int[] noOfVm = new int[run];
        for(int i = 0; i < run; i++){
            noOfVm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfUsedVm();
        }

        /* int sumNumOfVm = 0;
        for(int i = 0; i < run; i++){
           sumNumOfVm += noOfVm[i];
        }*/

        return noOfVm[run-1];
    }


    // SD VM 数量
    public double sdNoOfVm(){
        double aveNoOfVm = averageNoOfVm();
        double sd = 0.0;
        int[] noOfVm = new int[run];
        double sdSum = 0.0;
        for(int i = 0; i < run; i++){
            noOfVm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNoOfUsedVm();
        }

        for(int i = 0; i < run; i++){
            sdSum += noOfVm[i] * noOfVm[i];
        }

        sd = Math.sqrt(sdSum / run - aveNoOfVm * aveNoOfVm);
        return sd;
    }

    // PM 数量
    public int averageNoOfPm(){
        int[] noOfPm = new int[run];
        for(int i = 0; i < run; i++){
            noOfPm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNumOfPm();
        }

        /*int sumNumOfPm = 0;
        for(int i = 0; i < run; i++){
            sumNumOfPm += noOfPm[i];
        }*/

        return noOfPm[run-1];
    }


    // SD PM number
    public double sdNoOfPm(){
        double aveNoOfPm = averageNoOfPm();
        double sd = 0.0;
        int[] noOfPm = new int[run];
        double sdSum = 0.0;
        for(int i = 0; i < run; i++){
            noOfPm[i] = bestIndividualList.get((generation - 1) * (i + 1)).getNumOfPm();
        }

        for(int i = 0; i < run; i++){
            sdSum += noOfPm[i] * noOfPm[i];
        }

        sd = Math.sqrt(sdSum / run - aveNoOfPm * aveNoOfPm);
        return sd;
    }

    private double sumArray(double[] fitness){
        double sum = 0;
        for(int i = 0; i < fitness.length; i++){
            sum += fitness[i];
        }
        return sum;
    }



}
