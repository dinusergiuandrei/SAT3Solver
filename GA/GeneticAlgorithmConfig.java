package GA;

import GA.Generation;
import Logic.Function;

public class GeneticAlgorithmConfig{
    Double crossOverRate;
    Integer numberOfGenerations;
    Integer numberOfChromosomes;

    Generation currentGeneration;

    Double mutationRate;
    Function function;


    public GeneticAlgorithmConfig (
            Double               crossOverRate,
            Integer              numberOfGenerations,
            Integer              numberOfChromosomes,
            Generation           currentGeneration,
            Double               mutationRate,
            Function             function
    ){
        this.crossOverRate=crossOverRate;
        this.numberOfGenerations=numberOfGenerations;
        this.numberOfChromosomes=numberOfChromosomes;
        this.currentGeneration=currentGeneration;
        this.mutationRate=mutationRate;
        this.function=function;
    }
}
