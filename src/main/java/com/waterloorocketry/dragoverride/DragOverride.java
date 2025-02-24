//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.simulated.ReadCSV;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.simulation.SimulationConditions;
import info.openrocket.core.simulation.SimulationStatus;
import info.openrocket.core.simulation.exception.SimulationException;
import info.openrocket.core.simulation.extension.AbstractSimulationExtension;
import info.openrocket.core.simulation.listeners.AbstractSimulationListener;
import info.openrocket.core.util.Coordinate;


public class DragOverride extends AbstractSimulationExtension {
    public DragOverride() {
    }

    public void initialize(SimulationConditions conditions) throws SimulationException {
        conditions.getSimulationListenerList().add(new DragOverrideSimulationListener());

        ReadCSV readCSV = new ReadCSV();
        LazyMap<Double, AeroData, AeroData> dragData = readCSV.readCSV(this.getCSVFile());
    }

    public String getName() {
        return "DragOverride";
    }

    public String getId() {
        return "com.waterloorocketry.dragoverride";
    }

    @Override
    public String getDescription() {
        return "A plugin that overrides OpenRocket's drag coefficient based on a selected csv file.";
    }

    public String getCSVFile() {
        return config.getString("csvFile", "");
    }

    void setCSVFile(String csvFile) {
        config.put("csvFile", csvFile);
        fireChangeEvent();
    }
}
