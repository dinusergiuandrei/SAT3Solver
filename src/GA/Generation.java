package GA;

import Logic.Function;
import Logic.Literal;

import java.util.*;

public class Generation {
    Integer generationCount;
    private Integer populationSize;
    List<Chromosome> population = new LinkedList<>();
    Map<Chromosome, Double> chromosomeToFitnessMap = new LinkedHashMap<>();
    Chromosome bestChromosome;
    Function function;
    Double mutationRate;
    Double crossOverRate;

    public Generation(
            Integer populationSize,
            Function function,
            Double mutationRate,
            Double crossOverRate) {
        this.populationSize = populationSize;
        this.function = function;
        this.mutationRate=mutationRate;
        this.crossOverRate=crossOverRate;
        this.generationCount = 1;
    }

    public Generation(Generation anotherGeneration) {
        this.population.addAll(anotherGeneration.population);
        if (this.population.size() == 0)
            System.out.println("Help population");
        this.populationSize = anotherGeneration.populationSize;
        this.function = anotherGeneration.function;
        this.mutationRate=anotherGeneration.mutationRate;
        this.crossOverRate=anotherGeneration.crossOverRate;
        this.generationCount = anotherGeneration.generationCount + 1;
    }

    public void initializeFirstGeneration() {
        while (population.size() < populationSize)
            population.add(
                    new Chromosome(
                            Function.generateRandomBooleans(function.expression.numberOfLiterals),
                            function.numberOfLiterals
                    )
            );
    }

    public void computeChromosomeToFitnessMap() {
        Double[] maxScore = {-99999999999999999999.0};

        this.population.forEach(
                chromosome -> {
                    function.expression.updateLiterals(chromosome.assignation);
                    chromosomeToFitnessMap.put(
                            chromosome,
                            //function.expression.getNumberOfValidClauses()*1.0
                            function.expression.getNumberOfValidTerms()*1.0
                    );
                }
        );

        this.chromosomeToFitnessMap.forEach(
                (ch, f) -> {
                    if (f > maxScore[0]) {
                        maxScore[0] = f;
                        this.bestChromosome = ch;
                    }
                }
        );
        if (population.size() == 0)
            System.out.println("Null population");
        if (bestChromosome == null)
            System.out.println("Null best chromosome");
    }

    public Generation selectNextGeneration() {
        Generation newGeneration = new Generation(this);
        this.computeChromosomeToFitnessMap();
        Map<Chromosome, Double> chromosomeProbabilityMap = new LinkedHashMap<>();

        double[] sum = {0};
        double[] minFitness = {999999999999.0};

        this.chromosomeToFitnessMap.forEach(
                (ch, ff) -> {
                    if (ff < minFitness[0])
                        minFitness[0] = ff;
                }
        );

        this.chromosomeToFitnessMap.forEach(
                (ch, ff) -> {
                    sum[0] += (ff - minFitness[0]);
                }
        );

        if (sum[0] != 0.0) {
            this.chromosomeToFitnessMap.forEach(
                    (c, f) -> {
                        chromosomeProbabilityMap.put(c, (f - minFitness[0]) / sum[0]);
                    }
            );
        }
        else {
            this.chromosomeToFitnessMap.forEach(
                    (c, f) -> {
                        chromosomeProbabilityMap.put(c, 1.0);
                    }
            );
        }

        chromosomeProbabilityMap.forEach(
                (c, p) -> {
                    if (p.isNaN())
                        System.out.println("Stop");
                }
        );

        newGeneration.population.clear();
        Random random = new Random();

        while (newGeneration.population.size() < this.populationSize) {
            Double[] score = {random.nextDouble()};
            chromosomeProbabilityMap.forEach(
                    (ch, p) -> {
                        if (score[0] > 0) {
                            score[0] -= p;
                            if (score[0] < 0) {
                                newGeneration.population.add(new Chromosome(ch));
                            }
                        }
                    }
            );
        }

        return newGeneration;
    }

    private void applyMutations() {
        Random random = new Random();

        //this.function.computeLiteralToCountMap();

//        population.forEach(
//                chromosome -> {
//                    for (int i = 0; i < chromosome.numberOfLiterals; ++i) {
//                        if (random.nextDouble() < mutationRate)
//                            chromosome.doMutate(i);
//                    }
//                }
//        );

        this.function.computeLiteralToCountMap();

        for(int i=0; i<population.size(); ++i){
            Chromosome chromosome = population.get(i);
            for (int j = 0; j < chromosome.numberOfLiterals; ++j) {
                //this.function.expression.clauses.get(i);

                Literal literal = this.function.expression.getLiteral(j+1);

                // Daca este un bit relevant, va suferi mutatie mai rar
                Double c;
                Double avg = (3.0*this.function.expression.clauses.size()/this.function.expression.literals.size());
                c=this.function.literalToCountMap.get(literal)*1.0/avg;

//                if(this.function.literalToCountMap.get(literal)>avg)
//                    c=1.2;
//                else c=0.8;

                if (random.nextDouble() < mutationRate*c)  // /c
                    chromosome.doMutate(j);
            }
        }

    }

    private void applyCrossOver() {
        Map<Chromosome, Double> candidates = new LinkedHashMap<>();
        Random random = new Random();
        population.forEach(
                chromosome -> candidates.put(chromosome, random.nextDouble())
        );
        int i = 0, j = 0;
        while (i < population.size() && j < population.size()) {
            while (i < population.size() && candidates.get(population.get(i)) >= crossOverRate) i++;
            j = i + 1;
            while (j < population.size() && candidates.get(population.get(j)) >= crossOverRate) j++;

            if (!(i < population.size() && j < population.size())) {
                if (i < population.size()) {
                    if (random.nextDouble() < 0.5) {
                        //mutate chromosome i with the one with the one with the lowest mutation prob
                        Double minProb = 0.0;
                        int pos = 0;

                        for (int k = 0; k < population.size(); ++k) {
                            if (candidates.get(population.get(k)) < minProb && k!=i) {
                                minProb = candidates.get(population.get(k));
                                pos = k;
                            }
                        }
                        doCrossOver(i, pos);
                        break;
                    } else break;
                }
                break;
            }
            doCrossOver(i, j);
            i = j + 1;
        }
    }

    private void doCrossOver(Integer position1, Integer position2) {
        Random random = new Random();
        int cuttingPoint = (int) (random.nextDouble() * (function.numberOfLiterals));
        Chromosome chromosome1, chromosome2;
        chromosome1 = population.get(position1);
        chromosome2 = population.get(position2);
        Boolean aux;

        for (int i = cuttingPoint; i < function.numberOfLiterals; ++i) {
            aux = chromosome1.assignation.get(i);
            chromosome1.assignation.set(i, chromosome2.assignation.get(i));
            chromosome2.assignation.set(i, aux);
        }
    }

    public void applyGeneticOperators() {
        applyMutations();
        applyCrossOver();
    }



}