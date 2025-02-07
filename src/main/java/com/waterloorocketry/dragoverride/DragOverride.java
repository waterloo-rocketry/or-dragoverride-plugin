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

    public String getName() {
        String name;
        if (this.getLaunchVelocity() > 0.01) {
            name = this.trans.get("SimulationExtension.airstart.name.altvel");
        } else {
            name = this.trans.get("SimulationExtension.airstart.name.alt");
        }

        name = L10N.replace(name, "{alt}", UnitGroup.UNITS_DISTANCE.toStringUnit(this.getLaunchAltitude()));
        name = L10N.replace(name, "{vel}", UnitGroup.UNITS_VELOCITY.toStringUnit(this.getLaunchVelocity()));
        return name;
    }

    public String getDescription() {
        return "Start simulation with a configurable altitude and velocity";
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
