package com.waterloorocketry.dragoverride.simulated;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.util.LazyMap;


public class ReadCSV {
    public static void main(String[] args) {
        String csvFile = "./rockets/Aurora/CD_vs_MachNumber_2025-01-18.CSV";
        // Adjust the path as needed
        String delimiter = ",";
        Map<Double, AeroData> rawMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read the header line and create a mapping from column names to indices.
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("CSV file is empty.");
                return;
            }
            String[] headers = headerLine.split(delimiter);
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim(), i);
            }

            // Read each data row.
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(delimiter);
                // Use the header mapping to obtain the Mach value (key)
                double mach = Double.parseDouble(values[headerMap.get("Mach")].trim());
                AeroData aeroData = new AeroData(headerMap, values);
                rawMap.put(mach, aeroData);
            }

            // Wrap the raw map in a LazyMap. Here the computation function is the identity.
            LazyMap<Double, AeroData, AeroData> lazyMap = new LazyMap<>(rawMap, data -> data);

            // Example usage: print the AeroData for Mach 0.01
            System.out.println("AeroData for Mach 0.01: " + lazyMap.get(0.01));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}