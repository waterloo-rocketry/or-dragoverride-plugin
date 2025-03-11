package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.simulated.ReadCSV;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.simulation.exception.SimulationException;

public class InterpolatedCDCalculation {

    private LazyMap<Double, AeroData[], AeroData[]> dragData;

    public InterpolatedCDCalculation(LazyMap<Double, AeroData[], AeroData[]> dragData) {
        this.dragData = dragData;
    }

    public double calculatinginterpolatedCD(double alpha, double machSpeed, boolean engineStatus) throws SimulationException {
        double machStep = 0.01;

        // Round machSpeed to 2 decimal places
        double mach1 = Math.round(machSpeed * 100.0) / 100.0;
        double mach2 =  Math.round((mach1 + machStep) * 100.0) / 100.0;

        int alphaLow = (int) Math.floor(alpha);
        int alphaHigh = (int) Math.ceil(alpha);

        double alphaFraction = alpha - alphaLow;

        // Retrieve drag data
        AeroData[] aeroDataMach1 = this.dragData.get(mach1);
        AeroData[] aeroDataMach2 = this.dragData.get(mach2);

        // Get Cd values based on engine status at mach1
        double cdMach1AlphaLow = engineStatus ?
                aeroDataMach1[alphaLow].getCdPowerOn() :
                aeroDataMach1[alphaLow].getCdPowerOff();

        double cdMach1AlphaHigh = engineStatus ?
                aeroDataMach1[alphaHigh].getCdPowerOn() :
                aeroDataMach1[alphaHigh].getCdPowerOff();

        // Interpolate Cd at mach1 along alpha axis
        double cdMach1Interpolated = cdMach1AlphaLow + (cdMach1AlphaHigh - cdMach1AlphaLow) * alphaFraction;

        // Get Cd values based on engine status at mach2
        double cdMach2AlphaLow = engineStatus ?
                aeroDataMach2[alphaLow].getCdPowerOn() :
                aeroDataMach2[alphaLow].getCdPowerOff();

        double cdMach2AlphaHigh = engineStatus ?
                aeroDataMach2[alphaHigh].getCdPowerOn() :
                aeroDataMach2[alphaHigh].getCdPowerOff();

        // Interpolate Cd at mach2 along alpha axis
        double cdMach2Interpolated = cdMach2AlphaLow + (cdMach2AlphaHigh - cdMach2AlphaLow) * alphaFraction;

        // Finally, interpolate along mach axis
        double machFraction = (machSpeed - mach1) / (mach2 - mach1);

        return cdMach1Interpolated + (cdMach2Interpolated - cdMach1Interpolated) * machFraction;
    }

    public static void main(String[] args) throws SimulationException {
        // Example: Initialize your LazyMap with dummy data
        String csvFile = "rockets/Aurora/Cycle0_6inNozzle.csv";
        LazyMap<Double, AeroData[], AeroData[]> dragData = ReadCSV.readCSV(csvFile);

        // Initialize the calculation class
        InterpolatedCDCalculation calculator = new InterpolatedCDCalculation(dragData);

        // Example parameters
        double alpha = 0.32697011456580466;           // fractional alpha angle
        double machSpeed = 0.04549498581794866;     // Mach number requiring interpolation
        boolean engineStatus = true;  // engine power status (on/off)

        try {
            // Perform interpolation
            double interpolatedCD = calculator.calculatinginterpolatedCD(alpha, machSpeed, engineStatus);

            System.out.printf("Interpolated CD for alpha=%.2f, Mach=%.3f, engine status=%s: %.5f\n",
                    alpha, machSpeed, engineStatus ? "ON" : "OFF", interpolatedCD);
        } catch (SimulationException e) {
            System.err.println("Simulation exception: " + e.getMessage());
        }
    }

}
