package ProblemDefine;

import algorithms.CoEvaluate;
import algorithms.Constraint;

public abstract class CoGAProblemParameterSettings {
    private CoEvaluate evaluate;

    public CoGAProblemParameterSettings(CoEvaluate evaluate) {
        this.evaluate = evaluate;
    }
    public CoEvaluate getEvaluate(){ return evaluate; }
}
