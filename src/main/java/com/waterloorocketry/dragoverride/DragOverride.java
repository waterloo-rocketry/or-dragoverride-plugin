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
            calculatinginterpolatedCD(dragData, listener.getEngineStatus());
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

            if (aeroData == null || aeroData.length == 0) {
                throw new SimulationException("No AeroData found for Mach number: " + key);
            }

            mach1 = key;
            if (engineStatus) {
                cd1 = aeroData[0].getCdPowerOn();
            } else {
                cd1 = aeroData[0].getCdPowerOff();
            }

            if (i > 0) {
                if (mach1 == mach2) {
                    throw new SimulationException("Consecutive Mach numbers are the same. Unable to interpolate.");
                }
                System.out.println("cd1 is: " + cd1);
                System.out.println("cd2 is: " + cd2);
                System.out.println("mach1 is: " + mach2);
                System.out.println("mach2 is: " + mach1);

                interpolatedcd = cd1 + ((cd2-cd1)/0.01) * (mach1-mach2);
                double interpolatedmach = 0;
                interpolatedmach = (mach1 + mach2)/2;
                System.out.println("interpolatedcd is: " + interpolatedcd);

                // create new aerodata object with interpolated cd values
                AeroData insertdata = new AeroData();

                if (engineStatus) {
                    insertdata.setCdPowerOn(interpolatedcd);
                } else {
                    insertdata.setCdPowerOff(interpolatedcd);
                }
                System.out.println("Inserted interpolated AeroData: " + insertdata);
                System.out.println("interpolatedmach: " + interpolatedmach);

                dragData.put(interpolatedmach, new AeroData[]{insertdata});

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
