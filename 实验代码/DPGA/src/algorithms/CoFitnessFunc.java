/*
 * Boxiong Tan (Maximus Tann)
 * Title:        Single-objective GA framework
 * Description:  Single-objective GA framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * FitnessFunc.java - An abstract of common fitness function.
 */

package algorithms;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 *  FitnessFunc是unNormalizedFit实现的包装器。
 *  它主要使用java多线程框架来加速演化过程
 *  本质上，它仍然遵循工厂模式。它需要实现unnormaledfit抽象类的类类型，以便“新”计算实例，以便它们可以并行运行。
 */
public class CoFitnessFunc {

	@SuppressWarnings("rawtypes")
	private Class childType;
	@SuppressWarnings("rawtypes")
	/**
	 * 如果非规范化适应度函数没有实现UnNormalizedFit抽象类，则会引发异常。
	 * @param unNorFit 未规范化的fit抽象类的实现的类类型。
	 */
	public CoFitnessFunc(Class unNorFit){
		if(!CoUnNormalizedFit.class.isAssignableFrom(unNorFit)){
			throw new IllegalArgumentException("Class: " + unNorFit.getName() + " must "
					+ "implement CoUnNormalizedFit interface");
		}
		childType = unNorFit;
	}
	
    /**
     * execute方法是一个函数，它调用UnNormalizedFit抽象类的实现，然后返回一个fitness值列表。
     * 步骤:
     * 用这台机器的cpu数量初始化一个线程池
     * 使用反射创建UnNormalizedFit抽象类的实现的popSize。
     * 将这些任务添加到执行池中。
     * 执行所有任务并收集适应度值。
     *
     * @param popVar 种群变量
     * @return 一个数组<double[]> 适应度值
     * 		double[0] 适应度值,
     * 		double[1] 是种群中该适应度值的排名，该排名用当前染色体在种群中的位置初始化。因为它将在排序过程中使用。
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList<double[]> execute(int subPop, Chromosome [] popVar,
									   Chromosome[] representatives) {
		int popSize = popVar.length;
		
		// 创建线程池
		ExecutorService exec = Executors.newFixedThreadPool(
								Runtime.getRuntime().availableProcessors());
		
		// 任务数组
		ArrayList tasks = new ArrayList();
		
		// 创建实现的实例并将其添加到任务列表中
		for(int i = 0; i < popSize; i++){
			try {
				//获得构造函数后，需要传递该构造函数的类类型
				tasks.add(childType.getConstructor(int.class, Chromosome.class, Chromosome[].class)
						 .newInstance(subPop, popVar[i], representatives)
						 );
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		ArrayList<Future> results = null;
		try {
			// 执行任务
			results = (ArrayList<Future>) exec.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		exec.shutdown();
		
		ArrayList<double[]> fitness = new ArrayList<double[]>();
		int counter = 0;
		for(Future f: results){
			try {
				fitness.add((double[]) f.get());
				fitness.get(counter)[1] = counter;
				counter++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return fitness;
	}
}
