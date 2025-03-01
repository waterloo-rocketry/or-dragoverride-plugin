package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.aerodynamics.AerodynamicForces;
import info.openrocket.core.aerodynamics.FlightConditions;
import info.openrocket.core.simulation.listeners.AbstractSimulationListener;
import info.openrocket.core.simulation.SimulationStatus;
import info.openrocket.core.simulation.exception.SimulationException;

public class DragOverrideSimulationListener extends AbstractSimulationListener {
    private final LazyMap<Double, AeroData, AeroData> dragData;
    private boolean engineStatus = false;

    public DragOverrideSimulationListener(LazyMap<Double, AeroData, AeroData> dragData) {
        super();
        this.dragData = dragData;
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
        double currentMach = flightConditions.getMach();
        double roundedMach = Math.round(currentMach * 100.0) / 100.0;
        AeroData modifyData = dragData.get(roundedMach);
        double modifiedCd;

        if (this.thrust > 0.0) {
            if (!engineStatus) {
                engineStatus = true;
                System.out.println("====================================");
                System.out.println("========== Engine is on ===========");
                System.out.println("====================================");
            }
            modifiedCd = modifyData.getCdPowerOn();
        } else {
            if (engineStatus) {
                engineStatus = false;
                System.out.println("====================================");
                System.out.println("========== Engine is off ==========");
                System.out.println("====================================");
            }
            modifiedCd = modifyData.getCdPowerOff();
        }

        double originalCd = forces.getCD();

        forces.setCD(modifiedCd);

        double currentTime = status.getSimulationTime();
        System.out.println("Time: " + currentTime + " Mach: " + currentMach + " Cd(Org): " + originalCd + " Cd(Mod): " + modifiedCd + " Thrust: " + this.thrust);
        return forces;
    }
}
