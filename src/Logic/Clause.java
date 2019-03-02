package Logic;

import java.util.LinkedList;
import java.util.List;

public class Clause{
    public Integer numberOfLiterals;
    public List<Literal> literals = new LinkedList<>();
    public List<Boolean> coefficients = new LinkedList<>();

    public Clause(Integer numberOfLiterals, List<Boolean> coefficients, List<Literal> literals){
        this.numberOfLiterals = numberOfLiterals;
        this.coefficients=coefficients;
        this.literals=literals;

    }

    public void setLiterals(List<Literal> literals) {
        for (int i = 0; i < this.literals.size(); i++) {
            this.literals.set(i, literals.get(i));
        }
    }

    public Boolean evaluate(){
        Boolean result = false;
        for(int i=0; i<numberOfLiterals; ++i){
            result = result || (literals.get(i).value == coefficients.get(i));
        }
        return  result;
    }
}