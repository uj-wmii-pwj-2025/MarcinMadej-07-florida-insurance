package uj.wmii.pwj.w7.insurance;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


public class FloridaInsurance {

    public static void main(String[] args) {

        try {
            List<InsuranceProcessor.InsuranceEntry> entryList = InsuranceProcessor.loadZip("FL_insurance.csv.zip");
            generateCount(entryList);
            generateTotalTiv2012(entryList);
            generateMostValuable(entryList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void generateCount(List<InsuranceProcessor.InsuranceEntry> list) throws IOException {
        long count = list.stream()
                .map(e -> e.county)
                .distinct()
                .count();

        Files.writeString(
                Path.of("count.txt"),
                count + "",
                StandardCharsets.UTF_8
        );
    }

    public static void generateTotalTiv2012(List<InsuranceProcessor.InsuranceEntry> list) throws IOException {
        double totalTiv2012 = list.stream()
                .mapToDouble(e -> e.tiv2012)
                .sum();

        Files.writeString(
                Path.of("tiv2012.txt"),
                totalTiv2012 + "",
                StandardCharsets.UTF_8
        );
    }

    public static void generateMostValuable(List<InsuranceProcessor.InsuranceEntry> list) throws IOException {

        List<Map.Entry<String, Double>> top10tivDelta =
        list.stream()
        .collect(Collectors.groupingBy(
              e -> e.county,
              Collectors.summingDouble(e -> e.tivDelta)
        )).entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .limit(10).toList();

        StringBuilder sb = new StringBuilder();

        sb.append("country,value\n");

        for (Map.Entry<String, Double> entry : top10tivDelta) {
            sb.append(entry.getKey());
            sb.append(",");
            BigDecimal value = BigDecimal.valueOf(entry.getValue())
                    .setScale(2, RoundingMode.CEILING);
            sb.append(value);
            sb.append("\n");
        }

        Files.writeString(
                Path.of("most_valuable.txt"),
                 sb.toString(),
                StandardCharsets.UTF_8
        );
    }
 }

