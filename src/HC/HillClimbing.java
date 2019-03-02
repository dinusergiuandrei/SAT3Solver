package HC;

import Logic.Function;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import Logic.Literal;

public class HillClimbing{

    Integer numberOfLiterals;

    Strategy strategy;

    Double bestMinimum;

    Function function;

    Integer numberOfIterations;

    List<Literal> currentPoint;

    public boolean displayResults(Boolean verbose) {
        /*if(bestMinimum==0) {
            System.out.println(function.name + " is satisfiable. It is valid for:");
            for (int i = 0; i < currentPoint.size(); ++i) {
                System.out.println("l" + (i + 1) + " = " + currentPoint.get(i).value);
            }
            return true;
        }
        else {
            System.out.println(function.name + " is probably unsatisfiable. The most clauses are true for:");
            for (int i = 0; i < currentPoint.size(); ++i) {
                System.out.println("l" + (i + 1) + " = " + currentPoint.get(i).value);
            }
            return false;
        }*/
        if(bestMinimum==0) {
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

    public HillClimbing(HillClimbingConfig config) {
        this.numberOfLiterals=config.numberOfLiterals;
        this.strategy=config.strategy;
        this.function=config.function;
        this.numberOfIterations=config.numberOfIterations;
        this.currentPoint=config.currentPoint;
    }

    public void run() {
        Random random = new Random();
        bestMinimum = function.call(currentPoint);
        List<List<Literal>> candidatePoints;

        for (Integer i = 0; i < numberOfIterations; ++i) {

            candidatePoints = generateCandidatePoints(currentPoint);

            switch (this.strategy) {
                case BestImprovement:
                    for (List<Literal> candidatePoint : candidatePoints) {
                        Double result = function.call(candidatePoint);
                        if (result < bestMinimum) {
                            bestMinimum = result;
                            currentPoint = candidatePoint;
                        }
                    }
                    break;
                case FirstImprovement:
                    for (List<Literal> candidatePoint : candidatePoints) {
                        Double result = function.call(candidatePoint);
                        if (result < bestMinimum || random.nextDouble()<0.1) {
                            bestMinimum = result;
                            currentPoint = candidatePoint;
                            break;
                        }
                    }
                    break;
            }
        }
    }

    public List<List<Literal>> generateCandidatePoints(List<Literal> currentPoint) {
        List<List<Literal>> candidates = new ArrayList<>(this.numberOfLiterals);
        for (Integer i = 0; i < currentPoint.size(); ++i) {
                List<Literal> newPoint = new LinkedList<>();
                newPoint.addAll(currentPoint);
                newPoint.get(i).value = !newPoint.get(i).value;
                candidates.add(newPoint);

        }
        return candidates;
    }

    public enum Strategy {
        BestImprovement,
        FirstImprovement
    }

}
