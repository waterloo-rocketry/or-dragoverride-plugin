//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.waterloorocketry.dragoverride;

import info.openrocket.core.l10n.L10N;
import info.openrocket.core.simulation.SimulationConditions;
import info.openrocket.core.simulation.SimulationStatus;
import info.openrocket.core.simulation.exception.SimulationException;
import info.openrocket.core.simulation.extension.AbstractSimulationExtension;
import info.openrocket.core.simulation.listeners.AbstractSimulationListener;
import info.openrocket.core.unit.UnitGroup;
import info.openrocket.core.util.Coordinate;



public class DragOverride extends AbstractSimulationExtension {
    public DragOverride() {
    }

    public void initialize(SimulationConditions conditions) throws SimulationException {
        conditions.getSimulationListenerList().add(new DragOverride.DragOverrideListener());
    }

    public String getName()
    {
        return "DragOverride";
    }

    public String getId() {
        return "com.waterloorocketry.dragoverride";
    }

    @Override
    public String getDescription() {
        return "A plugin that overrides OpenRocket's drag coefficient based on a selected csv file.";
    }

    public double getLaunchAltitude() {
        return this.config.getDouble("launchAltitude", (double)100.0F);
    }

    public void setLaunchAltitude(double launchAltitude) {
        this.config.put("launchAltitude", launchAltitude);
        this.fireChangeEvent();
    }

    public double getLaunchVelocity() {
        return this.config.getDouble("launchVelocity", (double)50.0F);
    }

    public void setLaunchVelocity(double launchVelocity) {
        this.config.put("launchVelocity", launchVelocity);
        this.fireChangeEvent();
    }

    private class DragOverrideListener extends AbstractSimulationListener {
        private DragOverrideListener() {
        }

        public void startSimulation(SimulationStatus status) throws SimulationException {
            status.setRocketPosition(new Coordinate((double)0.0F, (double)0.0F, DragOverride.this.getLaunchAltitude()));
            status.setRocketVelocity(status.getRocketOrientationQuaternion().rotate(new Coordinate((double)0.0F, (double)0.0F, DragOverride.this.getLaunchVelocity())));
        }
    }
}
