package com.kerrrusha;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String INPUT_FILENAME = "input-denovo-ids.txt";
    private static final String OUTPUT_FILENAME = "output-query.txt";

    private static final String DEFAULT_SEARCHPOSITION = "1";
    private static final int DENOVO_IDS_AMOUNT = 200;

    public static void main(String[] args) {
        final Path inputPath = Paths.get(INPUT_FILENAME);
        System.out.println("Reading denovo ids from: " + inputPath.toAbsolutePath());

        final List<String> collectedDenovoIds = readRowByRow(inputPath);
        System.out.println("Collected " + collectedDenovoIds.size() + " denovo ids.");

        setDenovoIdsCorrectAmount(collectedDenovoIds);
        final String outputStr = "{\n" +
                "    \"debug\": true,\n" +
                "    \"array\": [\n" +
               getDenovoIdsJsonElements(collectedDenovoIds) +
                "    ]\n" +
                "}";

        final Path outputPath = Paths.get(OUTPUT_FILENAME);
        writeToFile(outputPath, outputStr);
        System.out.println("Exported result to: " + outputPath.toAbsolutePath());
    }

    private static void setDenovoIdsCorrectAmount(List<String> collectedDenovoIds) {
        if (collectedDenovoIds.size() < DENOVO_IDS_AMOUNT) {
            return;
        }
        final int elementsToRemoveAmount = collectedDenovoIds.size() - DENOVO_IDS_AMOUNT;
        if (elementsToRemoveAmount > 0) {
            collectedDenovoIds.subList(0, elementsToRemoveAmount).clear();
        }
        System.out.println("Removed " + elementsToRemoveAmount + " extra denovo ids. " +
                "Current amount: " + collectedDenovoIds.size());
    }

    private static String getDenovoIdsJsonElements(List<String> collectedDenovoIds) {
        final String ELEMENT_TEMPLATE = """
                {
                    "denovoId": %s,
                    "searchPosition": %s
                },""";

        StringBuilder result = new StringBuilder();
        collectedDenovoIds.forEach(elem -> result
                .append(String.format(ELEMENT_TEMPLATE, elem, DEFAULT_SEARCHPOSITION))
                .append(System.lineSeparator()));
        return result.toString();
    }

    private static void writeToFile(Path path, String output) {
        try {
            Files.write(path, output.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> readRowByRow(Path path) {
        List<String> rows = new ArrayList<>();
        try {
            rows = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rows;
    }
}