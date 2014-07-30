/*
 *
 *  Copyright (c) 2014, David Bruce Borenstein and the Trustees of
 *  Princeton University.
 *
 *  Except where otherwise noted, this work is subject to a Creative Commons
 *  Attribution (CC BY 4.0) license.
 *
 *  Attribute (BY): You must attribute the work in the manner specified
 *  by the author or licensor (but not in any way that suggests that they
 *  endorse you or your use of the work).
 *
 *  The Licensor offers the Licensed Material as-is and as-available, and
 *  makes no representations or warranties of any kind concerning the
 *  Licensed Material, whether express, implied, statutory, or other.
 *
 *  For the full license, please visit:
 *  http://creativecommons.org/licenses/by/4.0/legalcode
 * /
 */

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

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import layers.cell.CellIndex;
import test.EslimeTestCase;

public class CellIndexTest extends EslimeTestCase {

    private Coordinate p, q;
    private CellIndex index;

    protected void setUp() throws Exception {
        p = new Coordinate(0, 0, 0);
        q = new Coordinate(0, 0, Flags.BOUNDARY_APPLIED);

        index = new CellIndex();
    }

    public void testAdd() {
        assertTrue(index.add(p));
        assertFalse(index.add(q));

        index = new CellIndex();

        assertTrue(index.add(q));
        assertFalse(index.add(p));
    }

    public void testClear() {
        assertTrue(index.add(p));
        assertFalse(index.add(p));

        index.clear();

        assertTrue(index.add(p));
    }

    public void testContains() {
        assertFalse(index.contains(p));
        assertFalse(index.contains(q));

        index.add(p);

        assertTrue(index.contains(p));
        assertTrue(index.contains(q));
    }

    public void testRemove() {
        assertFalse(index.remove(q));
        index.add(p);
        assertTrue(index.remove(q));

        assertFalse(index.remove(p));
        index.add(q);
        assertTrue(index.remove(p));

    }
}
