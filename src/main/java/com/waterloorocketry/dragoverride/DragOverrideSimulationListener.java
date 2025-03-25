package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.aerodynamics.AerodynamicForces;
import info.openrocket.core.aerodynamics.FlightConditions;
import info.openrocket.core.simulation.listeners.AbstractSimulationListener;
import info.openrocket.core.simulation.SimulationStatus;
import info.openrocket.core.simulation.exception.SimulationException;

import java.io.BufferedWriter;

public class DragOverrideSimulationListener extends AbstractSimulationListener {
    private final LazyMap<Double, AeroData[], AeroData[]> dragData;
    private boolean engineStatus = false;
    private BufferedWriter csvWriter;
    InterpolatedCDCalculation calculator;

    public DragOverrideSimulationListener(LazyMap<Double, AeroData[], AeroData[]> dragData, BufferedWriter writer) {
        super();
        this.dragData = dragData;
        this.csvWriter = writer;
        this.calculator = new InterpolatedCDCalculation(dragData);
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

    public boolean getEngineStatus() {
        return engineStatus;
    }

    @Override
    public AerodynamicForces postAerodynamicCalculation(SimulationStatus status, AerodynamicForces forces) throws SimulationException {
        double currentTime = status.getSimulationTime();
        double currentMach = flightConditions.getMach();
        double roundedMach = Math.round(currentMach * 100.0) / 100.0;
        double absAlpha = Math.abs(flightConditions.getAOA());
        double originalCd = forces.getCD();
        double calculatedCd = calculator.calculatinginterpolatedCD(absAlpha, roundedMach, engineStatus);

        forces.setCD(calculatedCd);

        try {
            this.csvWriter.write(currentTime + "," + currentMach + "," + absAlpha + "," + originalCd + "," + calculatedCd + "," + this.thrust + "\n");
            this.csvWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return forces;
    }
}
