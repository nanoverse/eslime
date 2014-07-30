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

package layers;

import cells.MockCell;
import control.identifiers.Coordinate;
import layers.cell.CellLayerIndices;
import structural.CanonicalCellMap;
import test.EslimeTestCase;

public class CellLayerIndicesTest extends EslimeTestCase {

    private Coordinate c;
    private CellLayerIndices query;

    @Override
    protected void setUp() throws Exception {
        c = new Coordinate(0, 0, 0);
        query = new CellLayerIndices();
    }

    public void testNulltoNull() {
        query.refresh(c, null, null);

        assertFalse(query.isDivisible(c));
        assertFalse(query.isOccupied(c));
    }

    public void testNonNullToNull() {
        MockCell cell = new MockCell();
        cell.setDivisible(true);
        query.refresh(c, null, cell);

        query.refresh(c, cell, null);
        assertFalse(query.isIndexed(cell));
        assertFalse(query.isDivisible(c));
        assertFalse(query.isOccupied(c));
    }


    public void testNullToNonDivisible() {
        MockCell cell = new MockCell();

        cell.setDivisible(false);
        cell.setState(3);

        query.refresh(c, null, cell);

        assertTrue(query.isIndexed(cell));
        assertEquals(c, query.locate(cell));
        assertFalse(query.isDivisible(c));
        assertTrue(query.isOccupied(c));
        assertEquals((Integer) 1, query.getStateMap().get(3));
    }

    public void testNullToDivisible() {
        MockCell cell = new MockCell();
        cell.setDivisible(true);
        query.refresh(c, null, cell);
        assertTrue(query.isDivisible(c));
    }

    public void testNonNullTransition() {
        MockCell dCell = new MockCell();
        dCell.setState(5);
        dCell.setDivisible(true);

        MockCell nCell = new MockCell();
        nCell.setDivisible(false);
        nCell.setState(2);

        query.refresh(c, null, nCell);
        assertTrue(query.isIndexed(nCell));
        assertFalse(query.isIndexed(dCell));
        assertFalse(query.isDivisible(c));
        assertEquals((Integer) 0, query.getStateMap().get(5));
        assertEquals((Integer) 1, query.getStateMap().get(2));

        query.refresh(c, nCell, dCell);
        assertFalse(query.isIndexed(nCell));
        assertTrue(query.isIndexed(dCell));
        assertTrue(query.isDivisible(c));
        assertEquals((Integer) 1, query.getStateMap().get(5));
        assertEquals((Integer) 0, query.getStateMap().get(2));
    }

    public void testClone() {
        CanonicalCellMap map = new CanonicalCellMap(1);
        map.put(c, null);
        Object clone = query.clone(map);
        assertEquals(query, clone);
        assertFalse(query == clone);
    }
}
