package GA;

import java.util.ArrayList;
import java.util.List;

class Chromosome {
    List<Boolean> assignation;

    Integer numberOfLiterals;

    Chromosome(List<Boolean> assignation, Integer numberOfLiterals) {
        this.assignation = assignation;

        this.numberOfLiterals = numberOfLiterals;

    }

    Chromosome(Chromosome anotherChromosome) {

        this.numberOfLiterals = anotherChromosome.numberOfLiterals;

        this.assignation = new ArrayList<>();
        this.assignation.addAll(anotherChromosome.assignation);

    }

    void doMutate(Integer position) {
        this.assignation.set(position, !this.assignation.get(position));
    }

}
