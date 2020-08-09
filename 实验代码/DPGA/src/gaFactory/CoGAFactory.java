/*
 * Boxiong Tan (Maximus Tann)
 * Title:        GA algorithm framework
 * Description:  GA algorithm framework for general optimization purpose
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2016-2019, The Victoria University of Wellington
 * CoGAFactory.java - An interface of ga factory
 */


package gaFactory;
import algorithms.*;
import dataCollector.DataCollector;


public interface CoGAFactory {
    int getNumOfSubPop();
    InitPop[] getInitPopMethod();
	Mutation[] getMutation();
	Selection[] getSelection(int[] tournamentSize, int optimization);
	Crossover[] getCrossover();
	Sort[] getSort();
	DataCollector getDataCollector();
	Elitism[] getElitism(int[] elitSize, int optimization);
}
