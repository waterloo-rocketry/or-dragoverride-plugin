package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.simulation.exception.SimulationException;
import info.openrocket.core.aerodynamics.AerodynamicForces;

public class InterpolatedCDCalculation{
    public static double calculatinginterpolatedCD(LazyMap<Double, AeroData[], AeroData[]> dragData, boolean engineStatus, AerodynamicForces forces) throws SimulationException {
        int i = 0;
        double cd2 = 0;
        double mach2 = 0;
        double interpolatedcd = 0;
        double cd1, mach1;

        for (Double key : dragData.keySet()) {
            AeroData[] aeroData = dragData.get(key);

            mach1 = key;
            if (engineStatus) {
                cd1 = aeroData[0].getCdPowerOn();
            } else {
                cd1 = aeroData[0].getCdPowerOff();
            }

            if (i > 0) {
                System.out.println("cd1 is: " + cd1);
                System.out.println("cd2 is: " + cd2);
                System.out.println("mach1 is: " + mach2);
                System.out.println("mach2 is: " + mach1);

                interpolatedcd = cd1 + ((cd2-cd1)/0.01) * (mach1-mach2);
                System.out.println("interpolatedcd is: " + interpolatedcd);
                return interpolatedcd;

            }
            mach2 = mach1;
            cd2 = cd1;
            i++;
        }
        return interpolatedcd;
    }
}
