package gaFactory;

import ProblemDefine.*;
import java.util.ArrayList;
import GAProcedure.CoGA;
import algorithms.*;
import commonRepresentation.IntValueChromosome;


public class IntCoGA extends CoGA{
    private CoGAFactory factory;
    private CoGAParameterSettings pars;
    private CoGAProblemParameterSettings proSet;


    /**
     *
     * Constructor
     * @param pars Parameter settings
     * @param proSet Problem settings
     * @param factory factory is used to assemble parts
     *
     */
    public IntCoGA(CoGAParameterSettings pars, CoGAProblemParameterSettings proSet, CoGAFactory factory){
        this.factory = factory;
        this.pars = pars;
        this.proSet = proSet;
        prepare();
    }
    /**
     *
     * maxGen: 		最大迭代数
     * numOfSubPop  子种群数量
     * maxVar: 		一个粒子的最大变量数
     * lbounds      变量下界
     * ubounds       上界
     * popSize:		种群大小
     * optimization 最大化(1) 或最小化 (0)
     * popFit		population fitness
     * initPop		种群初始化
     * mutation		变异方法
     * crossover 	交叉方法
     * selection	选择方法
     * evaluate		评估
     * collector	data collector
     * sort   		排序
     */
    @Override
    protected void prepare(){
        numOfSubPop = pars.getNumOfSubPop();
        maxGen = pars.getMaxGen();
        maxVars = pars.getMaxVar();
        popSizes = pars.getPopSize();
        mutationRates = pars.getMutationRate();
        crossoverRates = pars.getCrossoverRate();
        lbounds = pars.getLbound();
        ubounds = pars.getUbound();
        optimization = pars.getOptimization();
        tournamentSizes = pars.getTournamentSize();
        elitSizes = pars.getElitSize();

        // This is a little bit tricky. We initial the PopFits outside the main flow
        popFits = new ArrayList[numOfSubPop];
        for(int i = 0; i < numOfSubPop; i++)
            popFits[i] = new ArrayList<double[]>();
        repFits = new double[numOfSubPop];
        // The same with popVars and representatives
        popVars = new ArrayList<Chromosome[]>();
        representatives = new Chromosome[numOfSubPop];
        for(int i = 0; i < numOfSubPop; i++)
            representatives[i] = new IntValueChromosome(maxVars[i]);

        initPops = factory.getInitPopMethod();
        mutations = factory.getMutation();
        crossovers = factory.getCrossover();
        elitisms = factory.getElitism(elitSizes, optimization);
        selections = factory.getSelection(tournamentSizes, optimization);
        evaluate = proSet.getEvaluate();
//        constraints = proSet.getConstraints();
        collector = factory.getDataCollector();
        sorts = factory.getSort();
    }


}
