package Logic;

import Logic.Clause;
import Logic.Literal;

import java.util.LinkedList;
import java.util.List;

public class LogicalExpression {
    public Integer numberOfLiterals;
    public Integer numberOfClauses;

    public List<Literal> literals = new LinkedList<>();

    public List<Clause> clauses = new LinkedList<>();

    public LogicalExpression(
            Integer numberOfLiterals,
            List<Literal> literals,
            Integer numberOfClauses,
            List<Clause> clauses
    ){
        this.numberOfLiterals = numberOfLiterals;
        this.numberOfClauses = numberOfClauses;
        this.clauses = clauses;
        this.literals = literals;
    }

    void setLiterals(List<Literal> otherLiterals){
        for (int i = 0; i < this.literals.size(); i++) {
            this.literals.set(i, otherLiterals.get(i));
        }
    }

    public Literal getLiteral(Clause clause, Integer position){
        return clause.literals.get(position);
    }

    public void updateLiterals(List<Boolean> newLiteralsValues){
        if(newLiteralsValues.size()!=this.numberOfLiterals)
            System.err.println("Wrong new literals!");
        for(int i=0; i<numberOfLiterals; ++i)
            this.literals.get(i).value=newLiteralsValues.get(i);
    }

    public Literal getLiteral(Integer id){
        for (int i = 0; i < this.literals.size(); i++) {
            if(this.literals.get(i).id==id)
                return this.literals.get(i);
        }
        return null;
    }

    public Boolean evaluate(){
        for (int i = 0; i < clauses.size(); i++) {
            if(clauses.get(i).evaluate()==false)
                return false;
        }
        return true;
    }

    public Integer getNumberOfValidClauses(){
        Integer result = 0;
        for (int i = 0; i < clauses.size(); i++) {
            if(clauses.get(i).evaluate()==true)
                result++;
        }
        return result;
    }

    public Integer getNumberOfValidTerms(){
        Integer result=0;
        for(int i=0; i<clauses.size(); ++i){
            for(int j=0; j<clauses.get(i).literals.size(); ++j)
                if(clauses.get(i).literals.get(j).value==clauses.get(i).coefficients.get(j))
                    ++result;
        }
        return result;
    }

}
