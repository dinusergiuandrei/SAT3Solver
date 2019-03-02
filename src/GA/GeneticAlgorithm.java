package GA;

import Logic.Function;

import java.util.*;

public class GeneticAlgorithm {
    Double crossOverRate;
    Double mutationRate;
    Function function;
    Integer numberOfGenerations;
    Integer numberOfChromosomes;
    Generation currentGeneration;
    public List<Generation> generationsDataBase;

    public GeneticAlgorithm(GeneticAlgorithmConfig config) {
        this.crossOverRate = config.crossOverRate;
        this.numberOfGenerations = config.numberOfGenerations;
        this.numberOfChromosomes = config.numberOfChromosomes;
        this.currentGeneration = config.currentGeneration;
        this.mutationRate = config.mutationRate;
        this.function = config.function;
    }

    public void run() {
        currentGeneration = new Generation(
                numberOfChromosomes,
                this.function,
                this.mutationRate,
                this.crossOverRate
        );
        currentGeneration.initializeFirstGeneration();

        generationsDataBase = new LinkedList<>();
        generationsDataBase.add(currentGeneration);

        while (!this.endingCondition()) {
            currentGeneration.computeChromosomeToFitnessMap();

            if (currentGeneration.population.size() == 0)
                System.out.println();

            Generation tmp = currentGeneration.selectNextGeneration();

            if (tmp.population.size() == 0)
                System.out.println();

            currentGeneration = tmp;

            currentGeneration.applyGeneticOperators();

            generationsDataBase.add(new Generation(currentGeneration));
            currentGeneration.computeChromosomeToFitnessMap();

        }

    }

    private Boolean endingCondition() {
        return generationsDataBase.size() > numberOfGenerations;
    }

    public Boolean displayResults(Boolean verbose) {
        currentGeneration.computeChromosomeToFitnessMap();
        function.expression.updateLiterals(currentGeneration.bestChromosome.assignation);

        if(function.expression.getNumberOfValidClauses()==function.expression.numberOfClauses){
            if(verbose==true)
                System.out.println("Satisfiable");
            return true;
        }
        else {
            if(verbose==true)
                System.out.println("Unsatisfiable");
            return false;
        }
    }
}
