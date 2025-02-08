// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.waterloorocketry.dragoverride;

import info.openrocket.core.plugin.Plugin;
import info.openrocket.core.simulation.extension.AbstractSimulationExtensionProvider;
import info.openrocket.core.simulation.extension.example.AirStart;

@Plugin
public class DragOverrideProvider extends AbstractSimulationExtensionProvider {
    public DragOverrideProvider() {
        super(DragOverride.class, new String[]{"Waterloo Rocketry", "Drag Override"});
    }
}
