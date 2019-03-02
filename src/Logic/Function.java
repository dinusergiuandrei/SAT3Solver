package Logic;

import Parser.ExpressionParser;

import java.io.FileNotFoundException;
import java.util.*;

public class Function {
    public LogicalExpression expression;

    public ExpressionParser parser;

    public Integer numberOfLiterals;
    public String name;

    public Map<Literal, Integer> literalToCountMap = new LinkedHashMap<>();

    public Function(String name, String filePath)  {
        this.name=name;
        try {
            this.parser = new ExpressionParser(filePath);
            this.expression = parser.getExpression(name);
            this.numberOfLiterals = this.expression.numberOfLiterals;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void computeLiteralToCountMap(){
        this.literalToCountMap.clear();

        for(int i=0; i<this.expression.clauses.size(); ++i){
            for(int j=0; j<this.expression.clauses.get(i).literals.size(); ++j){
                if(
                        this.literalToCountMap.containsKey(
                                this.expression.clauses.get(i)
                                        .literals.get(j)
                        )
                        ){
                    this.literalToCountMap.put(
                            this.expression.clauses.get(i).literals.get(j),
                            this.literalToCountMap.get(
                                    this.expression.clauses.get(i).literals.get(j)
                            ) +1
                    );
                }
                else this.literalToCountMap.put(
                        this.expression.clauses.get(i).literals.get(j),
                        0
                );
            }
        }
    }

    public Double call(List<Literal> params){
        this.expression.setLiterals(params);
        return this.expression.numberOfClauses-this.expression.getNumberOfValidClauses()*1.0;
    }

    public static List<Double> generateRandomDoubles(Integer n, Double intervalStart, Double intervalEnd){

        Random doubleGenerator = new Random();

        List<Double> params = new LinkedList<>();

        for(Integer i = 0; i<n; ++i){
            params.add(doubleGenerator.nextDouble()*(intervalEnd-intervalStart) + intervalStart);
        }
        return params;
    }

    public static List<Literal> generateRandomAssignationForLiterals(List<Literal> literals){
        Random random = new Random();
        for (int i = 0; i < literals.size(); i++) {
            if(random.nextDouble()<0.5)
                literals.get(i).value=false;
            else literals.get(i).value=true;
        }
        return literals;
    }

    public static List<Boolean> generateRandomBooleans(Integer n){
        List<Boolean> booleans = new LinkedList<>();
        Random random = new Random();
        for(int i=0; i<n; ++i){
            booleans.add(random.nextBoolean());
        }
        return booleans;
    }

    public static Double exp(Double base, Integer exp){
        Double result = 1.0;
        for(Integer i=1; i<=exp; ++i)
            result *= base;
        return result;
    }

    public static Integer getUpperBitCount(Integer x){
        Integer count = 0;
        if(x<0)
            count=1;
        x=Math.abs(x);
        while(x>0){
            count++;
            x=x/2;
        }
        return count;
    }
}
