package com.waterloorocketry.dragoverride;

import info.openrocket.core.aerodynamics.AerodynamicForces;
import info.openrocket.core.aerodynamics.FlightConditions;
import info.openrocket.core.simulation.listeners.AbstractSimulationListener;
import info.openrocket.core.simulation.SimulationStatus;
import info.openrocket.core.simulation.exception.SimulationException;

public class DragOverrideSimulationListener extends AbstractSimulationListener {
    public DragOverrideSimulationListener() {
        super();
    }

    private FlightConditions flightConditions = null;

    @Override
    public FlightConditions postFlightConditions(SimulationStatus status, FlightConditions flightConditions) throws SimulationException {
        this.flightConditions = flightConditions;
        return flightConditions;
    }

    @Override
    public AerodynamicForces postAerodynamicCalculation(SimulationStatus status, AerodynamicForces forces) throws SimulationException {
        final double velocityZ = status.getRocketVelocity().z;
//        System.out.println("Velocity: " + velocityZ);
        return forces;
    }
}
