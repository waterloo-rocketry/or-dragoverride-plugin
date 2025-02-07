package com.waterloorocketry.dragoverride;

import info.openrocket.core.document.Simulation;
import info.openrocket.core.plugin.Plugin;
import info.openrocket.core.simulation.extension.example.AirStart;
import info.openrocket.core.unit.UnitGroup;
import info.openrocket.swing.gui.SpinnerEditor;
import info.openrocket.swing.gui.adaptors.DoubleModel;
import info.openrocket.swing.gui.components.BasicSlider;
import info.openrocket.swing.gui.components.UnitSelector;
import info.openrocket.swing.simulation.extension.AbstractSwingSimulationExtensionConfigurator;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

@Plugin
public class DragOverrideConfigurator extends AbstractSwingSimulationExtensionConfigurator<AirStart> {

    protected DragOverrideConfigurator() {
        super(AirStart.class);
    }

    protected JComponent getConfigurationComponent(AirStart extension, Simulation simulation, JPanel panel) {
        panel.add(new JLabel("Hello World Launch altitude:"));
        DoubleModel m = new DoubleModel(extension, "LaunchAltitude", UnitGroup.UNITS_DISTANCE, (double)0.0F);
        JSpinner spin = new JSpinner(m.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "w 65lp!");
        UnitSelector unit = new UnitSelector(m, new Action[0]);
        panel.add(unit, "w 25");
        BasicSlider slider = new BasicSlider(m.getSliderModel((double)0.0F, (double)1000.0F));
        panel.add(slider, "w 75lp, wrap");
        panel.add(new JLabel("Launch velocity:"));
        m = new DoubleModel(extension, "LaunchVelocity", UnitGroup.UNITS_VELOCITY, (double)0.0F);
        spin = new JSpinner(m.getSpinnerModel());
        spin.setEditor(new SpinnerEditor(spin));
        panel.add(spin, "w 65lp!");
        unit = new UnitSelector(m, new Action[0]);
        panel.add(unit, "w 25");
        slider = new BasicSlider(m.getSliderModel((double)0.0F, (double)150.0F));
        panel.add(slider, "w 75lp, wrap");
        return panel;
    }
}
