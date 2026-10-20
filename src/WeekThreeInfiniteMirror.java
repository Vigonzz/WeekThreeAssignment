/* Violet Gonzalez Oct 16, 2026
 * Professor Cristina Verida Lopes
 * Part 2: Follow the Infinite Mirror style which
 * is modeled using induction, specifying the base case n0 and then n+1 rule
 * The program needs to:
 * " implement a recursive solution to the term frequency problem where parsing words
 * from the stream of characters is recursive. Much like the JSON parse function, your
 * parse function should take a character-level file reader, a list of words
 * (initially empty), and the list of stop words, and should return a list of the non-stop words.
 * The function should iterate to get the characters of one single word, and then recurse to get
 * the rest of the words."
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeekThreeInfiniteMirror {

    /*
     * Parse makes one recursive call per word, thus making the call stack grow with the size of the book.
     * If the default stack overflows then an error will appear: StackoverflowError so the solution
     * if to run this file with -Xss64m infront of it. More details in the README.
     */

    static List<String> parse(PushbackReader reader, List<String> wordsList, Set<String> stopWords) throws IOException {
        int character = reader.read();

        // BaseCase n0: peek, if the stream happens to be empty then return the accumulated list
        if (character == -1) {
            return wordsList;
        }
        reader.unread(character);

        // Skip non letters
        character = reader.read();
        character = Character.toLowerCase(character);

        // loop to skip if we are not at the end nor at a letter
        while (character != -1 && (character < 'a' || character > 'z')) {
            character = reader.read();
            character = Character.toLowerCase(character);
        }
        // Collect the letters and build the words
        StringBuilder buildingWord = new StringBuilder();
        while (character >= 'a' && character <= 'z') {
            buildingWord.append((char) character);
            character = reader.read();
            character = Character.toLowerCase(character);
        }

        // Turning the buildingWord into a string
        String finishedWord = buildingWord.toString();
        // kep check which adds one word to list with each parse call
        if (finishedWord.length() >= 2 && !stopWords.contains(finishedWord)) {
            wordsList.add(finishedWord);
        }

        // n+1 rule where one word is handled and parse answers for the rest of the stream
        return parse(reader, wordsList, stopWords);
    }


    public static void main(String[] args) throws IOException {
        // The reader
        PushbackReader readFile = new PushbackReader(new BufferedReader(new FileReader(args[0])));
        // list for the words
        List<String> words = new ArrayList<>();
        // holds the stopwprds
        Set<String> stopWords = new HashSet<>(Arrays.asList(Files.readString(Path.of("../stop_words.txt"))
                .split(",")));
        // holds all non stopwords
        List<String> wordsContainer = parse(readFile, words, stopWords);
        // Count the words
        Map<String, Long> wordFrequencies = wordsContainer.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Same as Code Golf: // Turning the map into a Stream, reversing order, then printing with a lambda to match exact output.
        wordFrequencies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(25)
                .forEach(entry -> System.out.println(entry.getKey() + "  -  "+ entry.getValue()));
    }
}
