package util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Created by cvasquez on 29.09.15.
 */
public class Statistics {

    public static void printStats(Collection<String> input) {
        Map<String, Long> collect =
                input.stream().collect(groupingBy(Function.identity(), counting()));
        DoubleSummaryStatistics result = collect.values().stream()
                .collect(Collectors.summarizingDouble((x) -> x));
        System.out.println(result);
    }

    public static void countAndPrintValues(Collection<String> input,long moreThan) {
//        Map<String, Long> counted = input.stream()
//                .collect(Collectors.groupingBy(o -> o, Collectors.counting()));
        Map<String, Long> collect =
                input.stream().collect(groupingBy(Function.identity(), counting()));

        for(String current:collect.keySet()){
            if (collect.get(current)>moreThan){
                System.out.println(collect.get(current) + "\t" + current);
            }
        }

    }

}
