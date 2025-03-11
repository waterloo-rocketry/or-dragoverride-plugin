package com.waterloorocketry.dragoverride.simulated;

import java.util.Map;

public class AeroData {
    private double alpha;
    private double cdPowerOff;
    private double cdPowerOn;

    public AeroData(double alpha, double cd, double cdPowerOff, double cdPowerOn, double caPowerOff, double caPowerOn, double cl, double cn, double cnPotential, double cnViscous, double cnAlpha, double cp, double cp0to4deg, double reynoldsNumber) {
        this.alpha = alpha;
        this.cdPowerOff = cdPowerOff;
        this.cdPowerOn = cdPowerOn;
    }

    public AeroData(Map<String, Integer> headerMap, String[] values) {
        // Using the header mapping ensures that the order doesn't matter.
        this.alpha          = Double.parseDouble(values[headerMap.get("Alpha")].trim());
        this.cdPowerOff     = Double.parseDouble(values[headerMap.get("CD_Off")].trim());
        this.cdPowerOn      = Double.parseDouble(values[headerMap.get("CD_On")].trim());
    }

    public AeroData(double alpha, double cdPowerOff, double cdPowerOn) {
        this.alpha = alpha;
        this.cdPowerOff = cdPowerOff;
        this.cdPowerOn = cdPowerOn;
    }

    public AeroData() {
        this.alpha = 0.0;
        this.cdPowerOff = 0.0;
        this.cdPowerOn = 0.0;
    }

    public double getCdPowerOff() {
        return this.cdPowerOff;
    }

    public double getCdPowerOn() {
        return this.cdPowerOn;
    }

    public void setCdPowerOn(double cdPowerOn) {
        this.cdPowerOn = cdPowerOn;
    }

    @Override
    public String toString() {
        return "AeroData{" +
                "alpha=" + alpha +
                ", cdPowerOff=" + cdPowerOff +
                ", cdPowerOn=" + cdPowerOn +
                '}';
    }
}