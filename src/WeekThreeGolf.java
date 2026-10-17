/* Violet Gonzalez Oct 16, 2026
* Professor Cristina Verida Lopes
* Part 1: Follow the golf code style which replicates
* a brevity practice
*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.* ;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


// This program is technically three lines but I seperated each step for better readability
public class WeekThreeGolf {
    public static void main (String[] args) throws IOException {
        // Load the stop words
        Set<String> stopWords = new HashSet<>(Arrays.asList(Files.readString(Path.of("../stop_words.txt")).split(",")));
        // Counting the text and storing it
        Map<String, Long> wordFrequencies = Pattern.compile("[a-z]{2,}")
                .matcher(Files.readString(Path.of(args[0])).toLowerCase())
                .results()
                .map(MatchResult::group)
                .filter(Predicate.not(stopWords::contains))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        // Turning the map into a Stream, reversing order, then printing with a lambda to match exact output.
        wordFrequencies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + "  -  "+ entry.getValue()));
    }
}
