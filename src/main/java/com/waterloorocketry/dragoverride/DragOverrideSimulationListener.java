package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.aerodynamics.AerodynamicForces;
import info.openrocket.core.aerodynamics.FlightConditions;
import info.openrocket.core.simulation.listeners.AbstractSimulationListener;
import info.openrocket.core.simulation.SimulationStatus;
import info.openrocket.core.simulation.exception.SimulationException;

import java.io.BufferedWriter;
import java.io.IOException;

public class DragOverrideSimulationListener extends AbstractSimulationListener {
    private final LazyMap<Double, AeroData[], AeroData[]> dragData;
    private boolean engineStatus = false;
    private BufferedWriter csvWriter;

    public DragOverrideSimulationListener(LazyMap<Double, AeroData[], AeroData[]> dragData, BufferedWriter writer) {
        super();
        this.dragData = dragData;
        this.csvWriter = writer;
    }

    private FlightConditions flightConditions = null;
    private double thrust = 0.0;

    @Override
    public FlightConditions postFlightConditions(SimulationStatus status, FlightConditions flightConditions) throws SimulationException {
        this.flightConditions = flightConditions;
        return flightConditions;
    }

    public double postSimpleThrustCalculation(SimulationStatus status, double thrust) throws SimulationException {
        this.thrust = thrust;
        return thrust;
    }

    @Override
    public AerodynamicForces postAerodynamicCalculation(SimulationStatus status, AerodynamicForces forces) throws SimulationException {
        double currentTime = status.getSimulationTime();
        double currentMach = flightConditions.getMach();
        double roundedMach = Math.round(currentMach * 100.0) / 100.0;
        double absAlpha = Math.abs(flightConditions.getTheta());
        int classifiedAlphaIndex = (absAlpha < 1) ? 0 : (absAlpha < 3) ? 1 : 2;
        AeroData modifyData = dragData.get(roundedMach)[classifiedAlphaIndex];
        double modifiedCd;

        double originalCd = forces.getCD();

        if (this.thrust > 0.0) {
            if (!engineStatus) {
                engineStatus = true;
                System.out.println("====================================");
                System.out.println("========== Engine is on ===========");
                System.out.println("====================================");
                System.out.println("Time: " + currentTime + " Thrust: " + this.thrust);
            }
            modifiedCd = modifyData.getCdPowerOn();
        } else {
            if (engineStatus) {
                engineStatus = false;
                System.out.println("====================================");
                System.out.println("========== Engine is off ==========");
                System.out.println("====================================");
                System.out.println("Time: " + currentTime + " Thrust: " + this.thrust);
            }
            modifiedCd = modifyData.getCdPowerOff();
        }

        forces.setCD(modifiedCd);

        try {
            csvWriter.write(currentTime + "," + currentMach + "," + absAlpha + "," + originalCd + "," + modifiedCd + "," + this.thrust + "\n");
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return forces;
    }
}
