import GA.Generation;
import GA.GeneticAlgorithm;
import GA.GeneticAlgorithmConfig;
import HC.HillClimbing;
import HC.HillClimbingConfig;
import Logic.Function;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//http://www.cs.ubc.ca/~hoos/SATLIB/benchm.html
public class Main{
    static Function function;
    static String filePath = "G:\\Projects\\HeuristicVsGA\\Expressions\\Satisfiable\\";

    static HillClimbing.Strategy strategy = HillClimbing.Strategy.FirstImprovement;
    static Integer numberOfIterations = 50;

    static Double crossOverRate = 0.85;
    static Double mutationRate=0.01;
    static Integer numberOfGenerations = 500;
    static Integer numberOfChromosomes = 30;
    static Integer numberOfRuns = 10;

    static List<String> paths = new LinkedList<>();
    static Map<String, Integer> foldersToSizeMap = new LinkedHashMap<>();


    public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {

        long startTime = System.currentTimeMillis();
        final String[] localPath = new String[1];
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

        computePaths();
        final int[] totalCorrectCountHC = {0};
        final int[] totalCorrectCountGA = {0};
        final int[] totalCount = {0};

        final int[] localCorrectCountHC = {0};
        final int[] localCorrectCountGA = {0};
        final int[] localCount = {0};

        foldersToSizeMap.forEach(
                (f, c) -> {
                    localCorrectCountGA[0]=0;
                    localCorrectCountHC[0]=0;
                    localCount[0]=0;

                    for(int i=1; i<=c; ++i) {
                        localPath[0] = f + "\\" + f + "-0"+i+".cnf";
                        System.out.println(localPath[0]);
                        paths.add(localPath[0]);

                        function = new Function(localPath[0], filePath);

                        if(runHillClimbing()==true) {
                            ++totalCorrectCountHC[0];
                            ++localCorrectCountHC[0];
                        }
                        if(runGeneticAlgorithm()==true) {
                            ++totalCorrectCountGA[0];
                            ++localCorrectCountGA[0];
                            //System.out.println("Yis");
                        }
                        ++totalCount[0];
                        ++localCount[0];

                    }
                    System.out.println(f +" : "+ localCount[0]+
                            "    ( "+((localCount[0]*1.0)/(paths.size()*1.0))+ " )    "+
                            (localCorrectCountHC[0]*1.0)/(localCount[0]*1.0)*100+" %      "+
                            (localCorrectCountGA[0]*1.0)/(localCount[0]*1.0)*100+" %");
                    writer.println(f +" : " + localCount[0]+
                            "    ( "+((localCount[0]*1.0)/(paths.size()*1.0))+ " )    "+
                            (localCorrectCountHC[0]*1.0)/(localCount[0]*1.0)*100+" %      "+
                            (localCorrectCountGA[0]*1.0)/(localCount[0]*1.0)*100+" %");
                }
        );

        long endTime = System.currentTimeMillis();

        System.out.println("|||||||||||||||||||||||||||||||");
        System.out.println("HC correct responses: "+ (totalCorrectCountHC[0]/totalCount[0]*100)+" %   " +
                totalCorrectCountHC[0]+"/"+totalCount[0]);
        System.out.println("GA correct responses: "+ (totalCorrectCountGA[0]/totalCount[0]*100)+" %   " +
                totalCorrectCountGA[0]+"/"+totalCount[0]);
        System.out.println(((endTime-startTime)/1000)+" seconds");

        writer.println("|||||||||||||||||||||||||||||||");
        writer.println("HC correct responses: "+ (totalCorrectCountHC[0]/totalCount[0]*100)+" %   " +
                        totalCorrectCountHC[0]+"/"+totalCount[0]);
        writer.println("GA correct responses: "+ (totalCorrectCountGA[0]/totalCount[0]*100)+" %   " +
                        totalCorrectCountGA[0]+"/"+totalCount[0]);
        writer.println(((endTime-startTime)/1000)+"seconds");

        writer.close();
    }

    public static boolean runHillClimbing(){
        for(int i=0; i<numberOfRuns; ++i) {
            HillClimbingConfig config = new HillClimbingConfig(
                    function.numberOfLiterals,
                    strategy,
                    function,
                    numberOfIterations,
                    Function.generateRandomAssignationForLiterals(function.expression.literals)
            );

            HillClimbing HC = new HillClimbing(config);

            HC.run();

            if(HC.displayResults(false)==true)
                return true;
        }
        return false;
    }

    public static boolean runGeneticAlgorithm(){
        for(int i=0; i<numberOfRuns; ++i) {
            GeneticAlgorithmConfig geneticAlgorithmConfig = new GeneticAlgorithmConfig(
                    crossOverRate,
                    numberOfGenerations,
                    numberOfChromosomes,
                    new Generation(
                            numberOfChromosomes,
                            function,
                            mutationRate,
                            crossOverRate),
                    mutationRate,
                    function
            );

            GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(geneticAlgorithmConfig);

            geneticAlgorithm.run();

            if(geneticAlgorithm.displayResults(false)==true)
                return true;
        }
        return false;
    }

    public static void computePaths(){
        foldersToSizeMap.put("uf20", 1000); //1000
//        foldersToSizeMap.put("uf50", 5); //1000
//        foldersToSizeMap.put("uf75", 100);
//        foldersToSizeMap.put("uf100", 100); //1000
//        foldersToSizeMap.put("uf125", 100);
//        foldersToSizeMap.put("uf150", 100);
//        foldersToSizeMap.put("uf175", 100);
//        foldersToSizeMap.put("uf200", 100);
//        foldersToSizeMap.put("uf225", 100);
//        foldersToSizeMap.put("uf250", 100);
    }
}
