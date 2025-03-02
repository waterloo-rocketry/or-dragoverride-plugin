//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.simulated.AeroData;
import com.waterloorocketry.dragoverride.simulated.ReadCSV;
import com.waterloorocketry.dragoverride.util.LazyMap;
import info.openrocket.core.simulation.SimulationConditions;
import info.openrocket.core.simulation.exception.SimulationException;
import info.openrocket.core.simulation.extension.AbstractSimulationExtension;

import java.io.BufferedWriter;
import java.io.FileWriter;


public class DragOverride extends AbstractSimulationExtension {
    public DragOverride() {
    }

    public void initialize(SimulationConditions conditions) throws SimulationException {
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
        calculatinginterpolatedCD(dragData, listener.getEngineStatus());
    }

    public String getName() {
        return "DragOverride";
    }

    @Override
    public String getDescription() {
        return "A plugin that overrides OpenRocket's drag coefficient based on a selected csv file.";
    }

    public void calculatinginterpolatedCD(LazyMap<Double, AeroData[], AeroData[]> dragData, boolean engineStatus) throws SimulationException {

        int i = 0;
        double cd2 = 0;
        double mach2 = 0;
        double cd1, mach1, interpolatedcd;

        // check if csv file is there
        if (dragData == null) {
            throw new SimulationException("CSV data is empty or not selected.");
        }
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
                System.out.println("mach1 is: " + mach1);
                System.out.println("mach2 is: " + mach2);

                interpolatedcd = cd1 + ((cd2-cd1)/0.01) * (mach2-mach1);
                System.out.println("interpolatedcd is: " + interpolatedcd);
            }

            mach2 = mach1;
            cd2 = cd1;

            i++;
        }
    }


    public String getCSVFile() {
        return config.getString("csvFile", "");
    }

    void setCSVFile(String csvFile) {
        config.put("csvFile", csvFile);
        fireChangeEvent();
    }
}
