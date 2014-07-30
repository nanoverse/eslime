/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package processes.discrete;

import cells.MockCell;
import control.arguments.ConstantDouble;
import control.halt.ExtinctionEvent;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.MockGeometry;
import geometry.set.CompleteSet;
import layers.MockLayerManager;
import layers.cell.CellLayer;
import processes.MockStepState;
import processes.discrete.check.CheckForExtinction;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

/**
 * Created by dbborens on 3/5/14.
 */
public class CheckForExtinctionTest extends EslimeTestCase {
    private MockGeometry geometry;
    private CellLayer layer;
    private MockLayerManager layerManager;
    private CheckForExtinction query;
    private MockGeneralParameters p;

    private void setup(Coordinate[] cc) {
        p = makeMockGeneralParameters();
        geometry = new MockGeometry();
        geometry.setCanonicalSites(cc);

        layer = new CellLayer(geometry);
        layerManager = new MockLayerManager();
        layerManager.setCellLayer(layer);
        query = new CheckForExtinction(null, layerManager, new CompleteSet(geometry), 0, new ConstantDouble(0.0), p);
    }

    private void doTest(boolean expected) {
        boolean actual = false;

        try {
            MockStepState state = new MockStepState();
            query.fire(state);
        } catch (ExtinctionEvent event) {
            actual = true;

            // Nothing else is supposed to be thrown!
        } catch (HaltCondition condition) {
            fail("Unexpected halt condition " + condition.getClass().getSimpleName());
        }

        assertEquals(expected, actual);

    }

    public void testExtinctionCase() {
        makeOneCanonicalSite();
        doTest(true);
    }

    public void testNonExtinctionCase() {
        populateSingletonCase();
        doTest(false);
    }

    /**
     * Make sure that, if there are occupied sites and they subsequently all
     * become vacant, this is still recorded as an exitnction event.
     */
    public void testTransitionToExtinction() {
        Coordinate coord = populateSingletonCase();
        doTest(false);
        layer.getUpdateManager().banish(coord);
        doTest(true);
    }

    private Coordinate populateSingletonCase() {
        makeOneCanonicalSite();
        Coordinate coord = new Coordinate(0, 0, 1);
        MockCell cell = new MockCell();
        cell.setState(1);
        layer.getUpdateManager().place(cell, coord);
        return coord;
    }

    private void makeOneCanonicalSite() {
        Coordinate[] cc = new Coordinate[]{
                new Coordinate(0, 0, 1)
        };
        setup(cc);
    }
}
