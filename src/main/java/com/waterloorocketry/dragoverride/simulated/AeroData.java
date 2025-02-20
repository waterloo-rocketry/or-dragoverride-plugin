package com.waterloorocketry.dragoverride.simulated;

import java.util.Map;

public class AeroData {
    private double alpha;
    private double cd;
    private double cdPowerOff;
    private double cdPowerOn;
    private double caPowerOff;
    private double caPowerOn;
    private double cl;
    private double cn;
    private double cnPotential;
    private double cnViscous;
    private double cnAlpha;
    private double cp;
    private double cp0to4deg;
    private double reynoldsNumber;    // Constructs an AeroData object from an array of CSV values.

    public AeroData(double alpha, double cd, double cdPowerOff, double cdPowerOn, double caPowerOff, double caPowerOn, double cl, double cn, double cnPotential, double cnViscous, double cnAlpha, double cp, double cp0to4deg, double reynoldsNumber) {
        this.alpha = alpha;
        this.cd = cd;
        this.cdPowerOff = cdPowerOff;
        this.cdPowerOn = cdPowerOn;
        this.caPowerOff = caPowerOff;
        this.caPowerOn = caPowerOn;
        this.cl = cl;
        this.cn = cn;
        this.cnPotential = cnPotential;
        this.cnViscous = cnViscous;
        this.cnAlpha = cnAlpha;
        this.cp = cp;
        this.cp0to4deg = cp0to4deg;
        this.reynoldsNumber = reynoldsNumber;
    }

    // Assumes values[0] is Mach (the key) and the rest follow in order.
    public AeroData(String[] values) {
        // values[1] -> Alpha, values[2] -> CD, values[3] -> CD Power-Off, etc.
        if (values.length != 15) {
            throw new IllegalArgumentException("CSV values must have 15 elements");
        }
        this.alpha = Double.parseDouble(values[1].trim());
        this.cd = Double.parseDouble(values[2].trim());
        this.cdPowerOff = Double.parseDouble(values[3].trim());
        this.cdPowerOn = Double.parseDouble(values[4].trim());
        this.caPowerOff = Double.parseDouble(values[5].trim());
        this.caPowerOn = Double.parseDouble(values[6].trim());
        this.cl = Double.parseDouble(values[7].trim());
        this.cn = Double.parseDouble(values[8].trim());
        this.cnPotential = Double.parseDouble(values[9].trim());
        this.cnViscous = Double.parseDouble(values[10].trim());
        this.cnAlpha = Double.parseDouble(values[11].trim());
        this.cp = Double.parseDouble(values[12].trim());
        this.cp0to4deg = Double.parseDouble(values[13].trim());
        this.reynoldsNumber = Double.parseDouble(values[14].trim());
    }

    public AeroData(Map<String, Integer> headerMap, String[] values) {
        // Using the header mapping ensures that the order doesn't matter.
        this.alpha          = Double.parseDouble(values[headerMap.get("Alpha")].trim());
        this.cd             = Double.parseDouble(values[headerMap.get("CD")].trim());
        this.cdPowerOff     = Double.parseDouble(values[headerMap.get("CD Power-Off")].trim());
        this.cdPowerOn      = Double.parseDouble(values[headerMap.get("CD Power-On")].trim());
        this.caPowerOff     = Double.parseDouble(values[headerMap.get("CA Power-Off")].trim());
        this.caPowerOn      = Double.parseDouble(values[headerMap.get("CA Power-On")].trim());
        this.cl             = Double.parseDouble(values[headerMap.get("CL")].trim());
        this.cn             = Double.parseDouble(values[headerMap.get("CN")].trim());
        this.cnPotential    = Double.parseDouble(values[headerMap.get("CN Potential")].trim());
        this.cnViscous      = Double.parseDouble(values[headerMap.get("CN Viscous")].trim());
        this.cnAlpha        = Double.parseDouble(values[headerMap.get("CNalpha (0 to 4 deg) (per rad)")].trim());
        this.cp             = Double.parseDouble(values[headerMap.get("CP")].trim());
        this.cp0to4deg      = Double.parseDouble(values[headerMap.get("CP (0 to 4 deg)")].trim());
        this.reynoldsNumber = Double.parseDouble(values[headerMap.get("Reynolds Number")].trim());
    }

    @Override
    public String toString() {
        return "AeroData{" +
                "alpha=" + alpha +
                ", cd=" + cd +
                ", cdPowerOff=" + cdPowerOff +
                ", cdPowerOn=" + cdPowerOn +
                ", caPowerOff=" + caPowerOff +
                ", caPowerOn=" + caPowerOn +
                ", cl=" + cl +
                ", cn=" + cn +
                ", cnPotential=" + cnPotential +
                ", cnViscous=" + cnViscous +
                ", cnAlpha=" + cnAlpha +
                ", cp=" + cp +
                ", cp0to4deg=" + cp0to4deg +
                ", reynoldsNumber=" + reynoldsNumber +
                '}';
    }
}