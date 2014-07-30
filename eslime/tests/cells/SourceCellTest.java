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

package cells;

import control.identifiers.Coordinate;

import java.util.HashMap;

/**
 * Created by dbborens on 1/2/14.
 */
public class SourceCellTest extends CellTest {

    private SourceCell a, b;

    @Override
    public void setUp() {
        HashMap<String, Double> aProduction = new HashMap<>();
        aProduction.put("alpha", 1.0);
        aProduction.put("beta", 2.0);

        HashMap<String, Double> bProduction = new HashMap<>();
        bProduction.put("beta", 0.5);

        a = new SourceCell(1, aProduction);
        b = new SourceCell(2, bProduction);
    }

    @Override
    public void testGetState() throws Exception {
        assertEquals(1, a.getState());
        assertEquals(2, b.getState());
    }

    @Override
    public void testGetHealth() throws Exception {
        assertEquals(0.0, a.getHealth(), epsilon);
        assertEquals(0.0, b.getHealth(), epsilon);
    }

    @Override
    public void testIsDivisible() throws Exception {
        // SourceCells are never divisible
        assertFalse(a.isDivisible());
        assertFalse(b.isDivisible());
    }

    @Override
    public void testFeedConsiderApply() throws Exception {
        // Feed, consider and apply should have no effect on SourceCells
        int result = a.consider();
        assertEquals(1, result);
    }

    @Override
    public void testDivide() throws Exception {
        boolean thrown = false;
        try {
            a.divide();
        } catch (Exception e) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    @Override
    public void testClone() throws Exception {
        // There should be two identical objects in memory, not one
        Cell c = a.clone();
        assertEquals(a, c);
        assert (a != c);
    }

    @Override
    public void testGetProduction() throws Exception {
        assertEquals(1.0, a.getProduction("alpha"));
        assertEquals(2.0, a.getProduction("beta"));
        assertEquals(0.5, b.getProduction("beta"));
    }

    public void testTriggerThrowsException() throws Exception {
        boolean thrown = false;

        try {
            a.trigger("a", new Coordinate(0, 0, 0));
        } catch (UnsupportedOperationException ex) {
            thrown = true;
        }
        assertTrue(thrown);
    }

}
