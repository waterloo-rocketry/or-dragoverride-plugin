package com.waterloorocketry.dragoverride;

import info.openrocket.core.document.Simulation;
import info.openrocket.core.plugin.Plugin;
import info.openrocket.swing.simulation.extension.AbstractSwingSimulationExtensionConfigurator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

@Plugin
public class DragOverrideConfigurator extends AbstractSwingSimulationExtensionConfigurator<DragOverride> {

    protected DragOverrideConfigurator() {
        super(DragOverride.class);
    }

    @Override
    protected JComponent getConfigurationComponent(DragOverride extension, Simulation simulation, JPanel panel) {
        // Set panel layout and padding
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Padding (top, left, bottom, right)
        panel.setPreferredSize(new Dimension(400, 150)); // Set default size

        // Label for CSV file selection
        JLabel fileLabel = new JLabel("Reference .csv file:");
        fileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label for displaying selected file path
        String csvPath = (extension.getCSVFile() != null) ? extension.getCSVFile() : "No file selected";
        JLabel selectedFileLabel = new JLabel(csvPath);
        selectedFileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Button for file selection
        JButton selectFileButton = new JButton("Select File");
        selectFileButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv"));

        selectFileButton.addActionListener(event -> {
            int returnValue = fileChooser.showOpenDialog(panel);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile != null) {
                    String selectedPath = selectedFile.getAbsolutePath();
                    extension.setCSVFile(selectedPath);
                    selectedFileLabel.setText(selectedPath);
                }
            }
        });

        // Add components to panel with spacing
        panel.add(fileLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Adds spacing
        panel.add(selectedFileLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(selectFileButton);

        return panel;
    }
}
