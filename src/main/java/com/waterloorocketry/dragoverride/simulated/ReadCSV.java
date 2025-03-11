package com.waterloorocketry.dragoverride.simulated;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.waterloorocketry.dragoverride.util.LazyMap;

public class ReadCSV {
    public static LazyMap<Double, AeroData[], AeroData[]> readCSV(String csvFile) {
        String delimiter = ",";
        // Allowed Alpha values.
        Set<Double> allowedAlphas = new HashSet<>(Arrays.asList(0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0));
        // Map that groups AeroData by Mach.
        Map<Double, List<AeroData>> machMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read header and build column-index mapping.
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("CSV file is empty.");
                return null;
            }
            String[] headers = headerLine.split(delimiter);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            // Process each data row.
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                // Parse Mach and Alpha.
                double mach = Double.parseDouble(values[headerMap.get("Mach")].trim());
                double alpha = Double.parseDouble(values[headerMap.get("Alpha")].trim());

                // Only include rows with allowed Alpha values.
                if (!allowedAlphas.contains(alpha)) {
                    continue;
                }

                AeroData aeroData = new AeroData(headerMap, values);
                machMap.computeIfAbsent(mach, k -> new ArrayList<>()).add(aeroData);
            }

            // Convert each list of AeroData into an array.
            Map<Double, AeroData[]> finalMap = new LinkedHashMap<>();
            for (Map.Entry<Double, List<AeroData>> entry : machMap.entrySet()) {
                List<AeroData> list = entry.getValue();
                if (list.size() != 11) {
                    System.err.println("Warning: For Mach " + entry.getKey()
                            + ", expected 11 AeroData entries for allowed Alpha values but found "
                            + list.size());
                }
                AeroData[] dataArray = list.toArray(new AeroData[list.size()]);
                finalMap.put(entry.getKey(), dataArray);
            }

            // Wrap the final map in a LazyMap using the identity function.
            return new LazyMap<>(finalMap, data -> data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args) {
        String csvFile = "rockets/Aurora/Cycle0_6inNozzle.csv";
        LazyMap<Double, AeroData[], AeroData[]> dragData = ReadCSV.readCSV(csvFile);

        if (dragData != null) {
            System.out.println("CSV Data Loaded Successfully");
            System.out.println("Mach values: " + dragData.keySet());
        } else {
            System.out.println("Failed to load CSV data");
        }
    }
}