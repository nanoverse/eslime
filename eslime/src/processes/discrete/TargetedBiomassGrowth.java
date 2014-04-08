/*
 * Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 * Princeton University.
 *
 * Except where otherwise noted, this work is subject to a Creative Commons
 * Attribution (CC BY 4.0) license.
 *
 * Attribute (BY): You must attribute the work in the manner specified
 * by the author or licensor (but not in any way that suggests that they
 * endorse you or your use of the work).
 *
 * The Licensor offers the Licensed Material as-is and as-available, and
 * makes no representations or warranties of any kind concerning the
 * Licensed Material, whether express, implied, statutory, or other.
 *
 * For the full license, please visit:
 * http://creativecommons.org/licenses/by/4.0/legalcode
 */

package processes.discrete;

import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;

import java.util.ArrayList;

/**
 * Adds a fixed amount of biomass to every cell with a cell
 * state matching the target.
 *
 * @author dbborens
 */
public class TargetedBiomassGrowth extends CellProcess {

    // How much biomass to accumulate per time step
    private double delta;

    // If false, apply() will be called on cells after their
    // biomass is updated. If true, you must call apply() on
    // a cell before the new biomass accumulates.
    private boolean defer;

    // Only adjustFitness cells if they are of the target type.
    private int targetCellType;

    public TargetedBiomassGrowth(ProcessLoader loader, LayerManager layerManager, int id,
                                 GeneralParameters p) {
        super(loader, layerManager, id, p);

        delta = Double.valueOf(get("delta"));

        defer = Boolean.valueOf(get("defer"));

        targetCellType = Integer.valueOf(get("target"));

    }

    public TargetedBiomassGrowth(LayerManager layerManager,
                                 double delta, boolean defer, int target) {
        super(null, layerManager, 0, null);

        this.delta = delta;
        this.defer = defer;
        this.targetCellType = target;
    }

    public void fire(StepState state) throws HaltCondition {

        // If the target state doesn't exist, don't waste any time
        // checking cells.
        int targetCount = layer.getViewer().getStateMapViewer().getCount(targetCellType);
        if (targetCount == 0) {
            return;
        }

        ArrayList<Coordinate> targetSites = new ArrayList<Coordinate>(targetCount);
        // Feed the cells.
        for (Coordinate site : activeSites) {
            if (layer.getViewer().isOccupied(site) && layer.getViewer().getCell(site).getState() == targetCellType) {
                layer.getViewer().getCell(site).adjustFitness(delta);
                targetSites.add(site);
            }
        }

        // If we're not defering updates, tell the cells to use the
        // new data right away. Note that we want to do this after
        // every cell has been "fed," in case there are non-local
        // interactions.
        if (!defer) {
            for (Coordinate site : targetSites) {
                layer.getUpdateManager().apply(site);
            }
        }

        // It would be annoying to highlight cells just for being fed, so we
        // don't.
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

}
