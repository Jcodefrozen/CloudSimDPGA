import algorithms.Chromosome;
import algorithms.Evaluate;
import algorithms.FitnessFunc;

import java.util.ArrayList;

public class EnergyEvaluation implements Evaluate{
    private ArrayList<FitnessFunc> funcList;

    public EnergyEvaluation(ArrayList<FitnessFunc> funcList){
        this.funcList = funcList;
    }

    public void setFuncList(ArrayList<FitnessFunc> funcList){
        this.funcList = funcList;
    }

    @Override
    public void evaluate(Chromosome[] popVar, ArrayList<double[]> fitness) {
        fitness.clear();
        ArrayList<ArrayList<double[]>> fitList = new ArrayList<>();

        for(int i = 0; i < funcList.size(); i++){
            ArrayList<double[]> tempFit = funcList.get(i).execute(popVar);
            fitList.add(tempFit);
        }

        for(int i = 0; i < popVar.length; i++){
            double[] fit = new double[2];
            // fitness value
            fit[0] = fitList.get(0).get(i)[0];

            // order
            fit[1] = i;
            fitness.add(fit);
            //-------Debug-----------
//            System.out.println("popVar.length = " + popVar.length);
//            System.out.println("fitness = " + fit[0] + ", order = " + fit[1]);
            //-------Debug-----------
        }
    }
}
