package Parser;

import Logic.Clause;
import Logic.Literal;
import Logic.LogicalExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ExpressionParser {
    String filePath;
    String fileName;

    Scanner scanner;

    public ExpressionParser(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
    }

    public LogicalExpression getExpression(String fileName) throws FileNotFoundException {
        Integer numberOfLiterals;
        Integer numberOfClauses;
        Integer clauseLength=3;
        List<Boolean> coefficients;
        List<Clause> clauses = new LinkedList<>();
        List<Literal> literals = new LinkedList<>();
        List<Literal> clauseLiterals;

        this.fileName = fileName;
        scanner = new Scanner(
                new File(this.filePath + this.fileName)
        );

        for (int i = 0; i < 7; i++) {
            scanner.nextLine();
        }

        String header = scanner.nextLine();

        Scanner scanner2 = new Scanner(header.substring(6));

        numberOfLiterals = scanner2.nextInt();

        numberOfClauses = scanner2.nextInt();

        for (int i = 1; i <= numberOfLiterals; ++i) {
            literals.add(new Literal(i, false));
        }

        int x, y, z;

        for (int i = 0; i < numberOfClauses; ++i) {
            coefficients = new LinkedList<>();
            clauseLiterals = new LinkedList<>();

            x = scanner.nextInt();
            y = scanner.nextInt();
            z = scanner.nextInt();

            scanner.nextInt();

            coefficients.add(x >= 0);
            coefficients.add(y >= 0);
            coefficients.add(z >= 0);

            clauseLiterals.add(getLiteral(x, literals));
            clauseLiterals.add(getLiteral(y, literals));
            clauseLiterals.add(getLiteral(z, literals));

            Clause clause = new Clause(clauseLength, coefficients, clauseLiterals);

            clauses.add(clause);
        }

        return new LogicalExpression(numberOfLiterals, literals, numberOfClauses, clauses);
    }

    public Literal getLiteral(Integer id, List<Literal> literals){
        for (int i = 0; i < literals.size(); i++) {
            if(literals.get(i).id== Math.abs(id))
                return literals.get(i);
        }
        return null;
    }
}
