package HC;

import HC.HillClimbing;
import Logic.Function;
import Logic.Literal;

import java.util.List;

public class HillClimbingConfig {

    Integer numberOfLiterals;

    HillClimbing.Strategy strategy;

    Double bestMinimum;

    Function function;

    Integer numberOfIterations;

    List<Literal> currentPoint;

    public HillClimbingConfig(
            Integer numberOfLiterals,
            HillClimbing.Strategy strategy,
            Function function,
            Integer numberOfIterations,
            List<Literal> startingPoint
    ) {

        this.numberOfLiterals = numberOfLiterals;

        this.strategy = strategy;

        this.function = function;

        this.numberOfIterations = numberOfIterations;

        this.currentPoint = startingPoint;

        this.bestMinimum = 9999999.0;

    }
}