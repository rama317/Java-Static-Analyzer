package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by laurynassakalauskas on 15/10/2016.
 */
public class ComplexityCounter {


    private static final int MAX_CYCLOMATIC_COMPLEXITY = Config.MAX_CYCLOMATIC_COMPLEXITY;

    private static final int MAX_WEIGHTED_COUNT = Config.MAX_WEIGHTED_COUNT;

    private final String classname;

    private HashMap<String, Integer> map = new HashMap<>();

    private HashMap<String, Integer> usage = new HashMap<>();

    public ComplexityCounter(String classname) {
        this.classname = classname;
    }


    /**
     * Collect information about each method
     */
    public void add(String  methodName, String type) {

        // check if such method was ever updated
        if (map.containsKey(methodName)) {

            map.put(methodName, map.get(methodName) + 1);
        } else {

            // method name was never queried. create a new HashMap
            map.put(methodName, 1);
        }

        // put information about different stats
        if (map.containsKey(type)) {

            map.put(type, map.get(type) + 1);
        } else {
            map.put(type, 1);
        }
    }

    public void analyze() {

        cyclomaticComplexity();
        weightedMethodCount();
    }


    /**
     * Calculate cyclomatic complexity for all methods
     */
    protected void cyclomaticComplexity() {
        for (Map.Entry<String, Integer> entry: map.entrySet()) {

            cyclomaticComplexity(entry.getKey());

        }
    }

    /**
     * Calculate cyclomatic complexity for selected method
     *
     * @param method
     * @return
     */
    public int cyclomaticComplexity(String method) {

        int sum = map.get(method);

        if (sum > MAX_CYCLOMATIC_COMPLEXITY) {
            Collector.getInstance().addWarning(classname, "\"" + method + "\" method has Cyclomatic complexity of more than " + MAX_CYCLOMATIC_COMPLEXITY + ". Consider minimizing class complexity.");
        }

        return sum;
    }


    /**
     * Calculate total weighted method count
     *
     * @return
     */
    protected int weightedMethodCount() {

        int sum = 0;

        for (Map.Entry<String, Integer> entry: map.entrySet()) {
            sum += entry.getValue();
        }

        sum = sum / map.entrySet().size();

        if (sum > MAX_WEIGHTED_COUNT) {
            Collector.getInstance().addWarning(classname, "Class has Weighted Method Count more than " + MAX_WEIGHTED_COUNT + ". Consider minimizing class complexity.");
        }

        return sum;
    }

}
