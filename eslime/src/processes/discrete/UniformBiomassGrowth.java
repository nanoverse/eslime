/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.Cell;
import control.GeneralParameters;
import control.arguments.Argument;
import control.arguments.ConstantDouble;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import factory.control.arguments.DoubleArgumentFactory;
import geometry.set.CoordinateSet;
import io.loader.ProcessLoader;
import layers.LayerManager;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.utilities.XmlUtil;

/**
 * Adds a fixed amount of biomass to every cell.
 *
 * @author dbborens
 */
public class UniformBiomassGrowth extends CellProcess {

    // How much biomass to accumulate per time step
    private Argument<Double> delta;

    // If false, apply() will be called on cells after their
    // biomass is updated. If true, you must call apply() on
    // a cell before the new biomass accumulates.
    private boolean defer;

    public UniformBiomassGrowth(ProcessLoader loader, LayerManager layerManager, CoordinateSet activeSites, int id,
                                GeneralParameters p) {
        super(loader, layerManager, activeSites, id, p);

//        delta = Double.valueOf(get("delta"));
        delta = DoubleArgumentFactory.instantiate(e, "delta", p.getRandom());
        defer = XmlUtil.getBoolean(e, "defer");

        if (defer) {
            throw new UnsupportedOperationException("Deferred actions are currently disabled.");
        }
    }

    public UniformBiomassGrowth(LayerManager layerManager, CoordinateSet activeSites,
                                double delta, boolean defer) {
        super(null, layerManager, activeSites, 0, null);

        this.delta = new ConstantDouble(delta);
        this.defer = defer;
    }

    @Override
    public void target(GillespieState gs) throws HaltCondition {
        // There's only one event that can happen--we update.
        if (gs != null) {
            gs.add(this.getID(), 1, 0.0D);
        }
    }

    @Override
    public void fire(StepState state) throws HaltCondition {
        // Feed the cells.
        for (Coordinate site : activeSites) {
            if (layer.getViewer().isOccupied(site)) {
                Cell cell = layer.getViewer().getCell(site);
                layer.getViewer().getCell(site).adjustHealth(delta.next());

            }
        }

        // If we're not defering updates, tell the cells to use the
        // new data right away. Note that we want to do this after
        // every cell has been "fed," in case there are non-local
        // interactions.
        if (!defer) {
            for (Coordinate site : activeSites) {
                if (layer.getViewer().isOccupied(site)) {
                    layer.getUpdateManager().apply(site);
                }
            }
        }
    }

}
