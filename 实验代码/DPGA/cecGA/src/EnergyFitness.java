/*
 * Boxiong Tan (Maximus Tann)
 * Title:        PSO algorithm framework
 * Description:  PSO algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * BPSOHaiTimeFitness.java - a response time fitness function for Hai's paper
 */

import java.util.ArrayList;
import java.util.HashMap;

import algorithms.*;

public class EnergyFitness extends UnNormalizedFit {
	/** 这些是数据，因此，被定义为静态值。 */
	private static double k;
	private static int numOfContainers;
	private static double pmCpu;
	private static double pmMem;
	private static double pmEnergy;
	private static int vmTypes;
	private static double[] vmCpu;
	private static double[] vmMem;
	private static double[] taskCpu;
	private static double[] taskMem;
	private static double vmCpuOverheadRate;
	private static double vmMemOverhead;

	public EnergyFitness(
							int numOfContainers,
							int vmTypes,
							double k,
							double pmCpu,
							double pmMem,
							double pmEnergy,
							double[] vmCpu,
							double[] vmMem,
							double[] taskCpu,
							double[] taskMem,
							double vmCpuOverheadRate,
							double vmMemOverhead
								){
		super(null);
		this.k = k;
		this.vmTypes = vmTypes;
		this.numOfContainers = numOfContainers;
		this.pmCpu = pmCpu;
		this.pmMem = pmMem;
		this.pmEnergy = pmEnergy;
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

	private void setVmListToIndividual(CecGAChromosome individual){

		ArrayList<ArrayList<Integer>> vmList = new ArrayList<>();
		ArrayList<int[]> vmList2 = new ArrayList<>();
		for(int i = 0; i < numOfContainers; i++){
		    ArrayList<Integer> vm;

		    // 虚拟机列表检查
		    if(vmList.isEmpty()) {
				vm = new ArrayList<>();
				vmList.add(vm);
			}

			// 检查这个VM是否已经创建
			if(vmList.size() - 1 >= individual.individual[i * 2 + 1]){
		    	vm = vmList.get(individual.individual[i * 2 + 1]);

		    } else {
		    	vm = new ArrayList<>();
		    	vmList.add(vm);
			}

			vm.add(i);
		}

		// 将列表转换为数组
		for(int i = 0; i < vmList.size(); i++){
			ArrayList<Integer> vm = vmList.get(i);
			int noOfContainer = vm.size();
			int[] vm2 = new int[noOfContainer];
			for(int j = 0; j < noOfContainer; j++){
				vm2[j] = vm.get(j);
			}
			vmList2.add(vm2);
		}

		individual.setVmList(vmList2);

	}

	public ArrayList<double[]> containerAllocation(CecGAChromosome individual){
		ArrayList<double[]> vmUsageList = new ArrayList<>();

		// 首先，计算使用了多少vm。
		int noOfVm = countVm(individual);

		// 其次，为VM的cpu和内存创建一个数组
		double[] vmCpus = new double[noOfVm];
		double[] vmMems = new double[noOfVm];

		// 并使用开销初始化它们
        for(int i = 0; i < noOfVm; i++){
        	int vmType = findType(individual, i);
        	vmCpus[i] = vmCpu[vmType] * vmCpuOverheadRate;
        	vmMems[i] = vmMemOverhead;
		}

		// 增加他们的用法
		for(int i = 0; i < numOfContainers; i++){
        	int vmIndex = individual.individual[i * 2 + 1];
        	vmCpus[vmIndex] += taskCpu[i];
        	vmMems[vmIndex] += taskMem[i];
		}

		// 最后，我们将它们转移到arrayList中
		for(int i = 0; i < noOfVm; i++){
        	double[] vmStatus = new double[2];
        	vmStatus[0] = vmCpus[i];
        	vmStatus[1] = vmMems[i];
        	vmUsageList.add(vmStatus);
		}


		individual.setVmStatusList(vmUsageList);
		return vmUsageList;
	}

	private double energy(ArrayList<double[]> vmUsage){
		double totalEnergy = 0;
		ArrayList<double[]> pmStatusList = new ArrayList<>();
		ArrayList<ArrayList<int[]>> pmList = new ArrayList<>();
		ArrayList<double[]> pmBoundList = new ArrayList<>();

		for(int i = 0; i < vmUsage.size(); i++){
			double[] vm = vmUsage.get(i);
			int vmType = findType((CecGAChromosome) individual, i);
			double[] pmStatus;
			double[] pmResBound;
			ArrayList<int[]> pm;
			if(pmStatusList.isEmpty()){
				pmStatus = new double[3];
				pmStatusList.add(pmStatus);
				pm = new ArrayList<>();
				pmList.add(pm);
				pmResBound = new double[2];
				pmBoundList.add(pmResBound);
			} else{
				// always get the last one
				pmStatus = pmStatusList.get(pmStatusList.size() - 1);
				pm = pmList.get(pmList.size() - 1);
				pmResBound = pmBoundList.get(pmBoundList.size() - 1);
			}

			if(pmResBound[0] + vmCpu[vmType] > pmCpu || pmResBound[1] + vmMem[vmType] > pmMem){
				pmStatus = new double[3];
				pmStatus[0] = vm[0];
				pmStatus[1] = vm[1];
				pmStatus[2] = 0;
				pmStatusList.add(pmStatus);

				pmResBound = new double[2];
				pmResBound[0] = vmCpu[vmType];
				pmResBound[1] = vmMem[vmType];
				pmBoundList.add(pmResBound);

				pm = new ArrayList<>();
				pmList.add(pm);


			} else{
				pmStatus[0] += vm[0];
				pmStatus[1] += vm[1];

				pmResBound[0] += vmCpu[vmType];
				pmResBound[1] += vmMem[vmType];
			}

			int[] vmStatus = new int[2];
			vmStatus[0] = i;
			vmStatus[1] = findType((CecGAChromosome) individual, i);
			pm.add(vmStatus);
		}

		for(int i = 0; i < pmStatusList.size(); i++){
			double energy = 0;
			energy = k * pmEnergy + (1 - k) * pmEnergy * (pmStatusList.get(i)[0] / pmCpu);
			pmStatusList.get(i)[2] = energy;
			totalEnergy += energy;
		}


		((CecGAChromosome)individual).setPmStatusList(pmStatusList);
		((CecGAChromosome)individual).setPmList(pmList);
		return totalEnergy;
	}

	public Object call() throws Exception {
	    double[] fit = new double[2];

	    setVmListToIndividual((CecGAChromosome) individual);
		// 计算每个VM的cpu使用情况
        ArrayList<double[]> vmUsage = containerAllocation((CecGAChromosome)individual);


		// calculate energy consumption
		double energy = energy(vmUsage);

		((CecGAChromosome)individual).setFitness(energy);
        fit[0] = energy;
        fit[1] = 0;
        return fit;
	}



	private Integer findType(CecGAChromosome individual, int index){
		Integer vmType = null;
		for(int i = 0; i < numOfContainers; ++i){
			if(individual.individual[i * 2 + 1] == index){
				vmType = individual.individual[i * 2];
				break;
			}
		}
		if(vmType == null){
			System.out.println("Wrong!!!!!");
		}
		return vmType;
	}

	// 计算vm的最大数量
	private int countVm(CecGAChromosome individual){
		int numOfVm = 0;
		for(int i = 0; i < numOfContainers; ++i){
			if(numOfVm < individual.individual[i * 2 + 1]) numOfVm = individual.individual[i * 2 + 1];
		}
		return numOfVm + 1;
	}

	/**
	 *
	 * @param number
	 * @return
	 */
	private double round5(double number){
		number = Math.floor(number * 100000) / 100000;
		return number;
	}


}
