package com.waterloorocketry.dragoverride;

import com.waterloorocketry.dragoverride.DragOverride;
import info.openrocket.core.document.Simulation;
import info.openrocket.core.plugin.Plugin;
import info.openrocket.core.unit.UnitGroup;
import info.openrocket.swing.gui.SpinnerEditor;
import info.openrocket.swing.gui.adaptors.DoubleModel;
import info.openrocket.swing.gui.components.BasicSlider;
import info.openrocket.swing.gui.components.UnitSelector;
import info.openrocket.swing.simulation.extension.AbstractSwingSimulationExtensionConfigurator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@Plugin
public class DragOverrideConfigurator extends AbstractSwingSimulationExtensionConfigurator<DragOverride> {

    protected DragOverrideConfigurator() {
        super(DragOverride.class);
    }

    protected JComponent getConfigurationComponent(DragOverride extension, Simulation simulation, JPanel panel) {
        panel.add(new JLabel("Reference .csv file:"));

        JButton selectFile = new JButton("Select File");
        JFileChooser fileChooser = new JFileChooser();
        // prevents directories from being selected
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // shows only csv files
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        selectFile.addActionListener(event -> {
            fileChooser.showOpenDialog(panel);
        });

        panel.add(selectFile);
        return panel;
    }
}
