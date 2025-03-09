//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.simulated.ReadCSV;
import com.waterloorocketry.dragoverride.util.LazyMap;
import com.waterloorocketry.dragoverride.InterpolatedCDCalculation;
import info.openrocket.core.simulation.SimulationConditions;
import info.openrocket.core.simulation.exception.SimulationException;
import info.openrocket.core.simulation.extension.AbstractSimulationExtension;
import info.openrocket.core.aerodynamics.AerodynamicForces;


import java.io.BufferedWriter;
import java.io.FileWriter;


public class DragOverride extends AbstractSimulationExtension {
    public DragOverride() {
    }

    public void initialize(SimulationConditions conditions) throws SimulationException {
        try {
            LazyMap<Double, AeroData[], AeroData[]> dragData = ReadCSV.readCSV(this.getCSVFile());
            BufferedWriter writer = null;

            try {
                writer = new BufferedWriter(new FileWriter("simu-out.csv"));
                writer.write("CurrentTime,Mach,Alpha(AOA),CdOrg,CdMod,Thrust\n");
            } catch (Exception e) {
                System.out.println("Error creating csv");
            }

            DragOverrideSimulationListener listener = new DragOverrideSimulationListener(dragData, writer);
            conditions.getSimulationListenerList().add(listener);
//            InterpolatedCDCalculation.calculatinginterpolatedCD(dragData, listener.getEngineStatus(), new AerodynamicForces());
        } catch (Exception e) {
            e.printStackTrace();
            throw new SimulationException("error is.. " + e.getMessage(),e);

        }
    }

    public String getName() {
        return "DragOverride";
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
