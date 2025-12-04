/************************************************************************
 * @file: Proj4.java
 * @description: Reads data from the "steam_games.csv" dataset, and tests the runtimes of
 * sorted, shuffled, and reversed configurations when inserting, searching, and removing
 * all games from that list in a SeparateChainingHashTable
 * @author: Will S
 * @date: December 3, 2025
 ************************************************************************/

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Proj4 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java Proj4 <input file> <number of lines>");
            System.exit(1);
        }

        String inputFileName = args[0].strip().toLowerCase();
        int numLines = Integer.parseInt(args[1].strip());

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

        ArrayList<String> stringList = createStringList(inputFileName, numLines-1);
        ArrayList<Game> steam_games = stringToGame(stringList);

        analysis(steam_games, numLines);
    }

    //METHODS USING CODE FORKED FROM "Proj3.java" IN PROJ3
    public static void analysis(ArrayList<Game> steam_games, int numLines) throws FileNotFoundException {
        FileOutputStream analysis = new FileOutputStream("analysis.csv", true);
        PrintWriter analysisWriter = new PrintWriter(analysis);
        //only run this next line when creating a new analysis.csv file
        //analysisWriter.println("numLines,runtimeSorted,runtimeShuffled,runtimeReversed");

        Collections.sort(steam_games);

        analysisWriter.print(numLines);

        long start;
        long end;

        //Sorted (best case)
        start = System.nanoTime();
        hashbrown(steam_games, numLines);
        end = System.nanoTime();

        System.out.println("Sorted Runtime: " + (end - start) / 1000000000.0 + " seconds");

        analysisWriter.print("," + (end - start) / 1000000000.0);

        //Shuffled (average case)
        Collections.shuffle(steam_games);

        start = System.nanoTime();
        hashbrown(steam_games, numLines);
        end = System.nanoTime();

        System.out.println("Shuffled Runtime: " + (end - start) / 1000000000.0 + " seconds");

        analysisWriter.print("," + (end - start) / 1000000000.0);

        //Reversed (worst case)
        Collections.sort(steam_games, Collections.reverseOrder());

        start = System.nanoTime();
        hashbrown(steam_games, numLines);
        end = System.nanoTime();

        System.out.println("Reversed Runtime: " + (end - start) / 1000000000.0 + " seconds");

        analysisWriter.print("," + (end - start) / 1000000000.0);

        analysisWriter.println();

        analysisWriter.flush();
        analysisWriter.close();
    }

    public static void hashbrown(ArrayList<Game> steam_games, int numLines) {
        SeparateChainingHashTable<Game> hashTable;

        hashTable = new SeparateChainingHashTable<>(numLines);
        for (Game game : steam_games) {
            hashTable.insert(game);
        }
        for (Game game : steam_games) {
            hashTable.contains(game);
        }
        for (Game game : steam_games) {
            hashTable.remove(game);
        }
    }




    // METHODS FROM "GenerateList.java" IN PROJ3

    //Creates a list of games based on the input file
    //Arguments: File name, number of lines to read
    public static ArrayList<String> createStringList(String filename, int numLines) throws FileNotFoundException {
        Scanner csvReader = new Scanner(new File(filename));
        csvReader.nextLine(); //skip the header line
        ArrayList<String> lines = new ArrayList<>();
        while (csvReader.hasNextLine() && numLines >= 0) {
            String line = csvReader.nextLine();
            if (!line.isEmpty()) {
                lines.add(line);
                numLines--;
            }
        }
        return lines;
    }

    //takes the array of Strings, processes them as games, and inserts into the ArrayList
    public static ArrayList<Game> stringToGame(ArrayList<String> stringList) {
        ArrayList<Game> gameList = new ArrayList<>();
        for (String line : stringList) {
            Game toAdd = processLine(line);
            if (toAdd != null) gameList.add(toAdd);
        }
        System.out.println("\nArrayList populated with " + gameList.size() + " games.");
        return gameList;
    }

    public static Game processLine(String line) {
        String[] data = line.split(",");
        Game processed = null;
        try {
            processed = (new Game(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3], Integer.parseInt(data[4]),
                    Integer.parseInt(data[5]), Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), Double.parseDouble(data[9])));
        } catch (NumberFormatException e) {
            System.out.println("Error with parsing line: " + line);
        }
        return processed;
    }
}
