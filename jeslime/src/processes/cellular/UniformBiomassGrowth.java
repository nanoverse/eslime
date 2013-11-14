package processes.cellular;

import java.util.HashSet;

import org.dom4j.Element;

import geometry.Geometry;
import io.project.ProcessLoader;
import processes.StepState;
import processes.gillespie.GillespieState;
import structural.GeneralParameters;
import layers.cell.CellLayer; import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

/**
 * Adds a fixed amount of biomass to every cell.
 * 
 * @author dbborens
 *
 */
public class UniformBiomassGrowth extends CellProcess {

	// How much biomass to accumulate per time step
	private double delta;
	
	// If false, apply() will be called on cells after their
	// biomass is updated. If true, you must call apply() on
	// a cell before the new biomass accumulates.
	private boolean defer;
	
	public UniformBiomassGrowth(ProcessLoader loader, CellLayer layer, int id,
			Geometry geom, GeneralParameters p) {
		super(loader, layer, id, geom, p);
		
		delta = Double.valueOf(get("delta"));
		
		defer = Boolean.valueOf(get("defer"));
	}

	public UniformBiomassGrowth(CellLayer layer, Geometry geom, 
			double delta, boolean defer) {
		super(null, layer, 0, geom, null);
		
		this.delta = delta;
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
				layer.getViewer().getCell(site).feed(delta);
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
		
		// It would be annoying to highlight cells just for being fed, so we
		// don't.
	}

}