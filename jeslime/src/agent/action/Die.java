package agent.action;

import cells.BehaviorCell;
import layers.LayerManager;
import structural.identifiers.Coordinate;

/**
 * Created by dbborens on 2/10/14.
 */
public class Die extends Action {
    public Die(BehaviorCell callback, LayerManager layerManager) {
        super(callback, layerManager);
    }

    @Override
    public void run(Coordinate caller) {
        getCallback().die();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Die) {
            return true;
        }
        return false;
    }

    @Override
    public Action clone(BehaviorCell child) {
        return new Die(child, getLayerManager());
    }
}