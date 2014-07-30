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

package geometry.shape;

import control.identifiers.Coordinate;
import control.identifiers.Flags;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import test.EslimeTestCase;

public class RectangleTest extends EslimeTestCase {

    private Shape odd;
    private Shape even;

    private Lattice evenLattice;

    @Override
    public void setUp() {
        Lattice oddLattice = new RectangularLattice();
        evenLattice = new RectangularLattice();

        even = new Rectangle(evenLattice, 2, 4);
        odd = new Rectangle(oddLattice, 3, 5);
    }

    public void testGetCenter() {
        Coordinate actual, expected;

        // Even -- we round down
        expected = new Coordinate(0, 1, 0);
        actual = even.getCenter();
        assertEquals(expected, actual);

        // Odd
        expected = new Coordinate(1, 2, 0);
        actual = odd.getCenter();
        assertEquals(expected, actual);
    }

    public void testGetBoundaries() {
        Coordinate[] actual, expected;

        // Even
        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(0, 3, 0),
                new Coordinate(1, 3, 0),
                new Coordinate(1, 2, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(1, 0, 0)
        };
        actual = even.getBoundaries();
        assertArraysEqual(actual, expected, true);

        // Odd
        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(0, 3, 0),
                new Coordinate(0, 4, 0),
                new Coordinate(1, 4, 0),
                new Coordinate(2, 4, 0),
                new Coordinate(2, 3, 0),
                new Coordinate(2, 2, 0),
                new Coordinate(2, 1, 0),
                new Coordinate(2, 0, 0),
                new Coordinate(1, 0, 0)
        };
        actual = odd.getBoundaries();
        assertArraysEqual(actual, expected, true);
    }

    public void testCanonicalSites() {
        Coordinate[] actual, expected;

        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(0, 3, 0),
                new Coordinate(1, 3, 0),
                new Coordinate(1, 2, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(1, 0, 0)
        };
        actual = even.getCanonicalSites();
        assertArraysEqual(actual, expected, true);
    }

    public void testOverbounds() {
        Coordinate actual, expected;

        // Test coordinates -- in bounds
        Coordinate a, b, c;

        a = new Coordinate(0, 0, 0);
        b = new Coordinate(0, 2, 0);
        c = new Coordinate(1, 1, 0);

        // Test coordinates -- out of bounds
        Coordinate p, q, r, s;
        p = new Coordinate(3, 0, 0);
        q = new Coordinate(0, 6, 0);
        r = new Coordinate(-3, 0, 0);
        s = new Coordinate(-1, 5, 0);

        // Even
        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(a);
        assertEquals(actual, expected);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(b);
        assertEquals(actual, expected);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = even.getOverbounds(c);
        assertEquals(actual, expected);

        expected = new Coordinate(2, 0, Flags.VECTOR);
        actual = even.getOverbounds(p);
        assertEquals(actual, expected);

        expected = new Coordinate(0, 3, Flags.VECTOR);
        actual = even.getOverbounds(q);
        assertEquals(actual, expected);

        expected = new Coordinate(-3, 0, Flags.VECTOR);
        actual = even.getOverbounds(r);
        assertEquals(actual, expected);

        expected = new Coordinate(-1, 2, Flags.VECTOR);
        actual = even.getOverbounds(s);
        assertEquals(actual, expected);

        // Odd
        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(a);
        assertEquals(actual, expected);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(b);
        assertEquals(actual, expected);

        expected = new Coordinate(0, 0, Flags.VECTOR);
        actual = odd.getOverbounds(c);
        assertEquals(actual, expected);

        expected = new Coordinate(1, 0, Flags.VECTOR);
        actual = odd.getOverbounds(p);
        assertEquals(actual, expected);

        expected = new Coordinate(0, 2, Flags.VECTOR);
        actual = odd.getOverbounds(q);
        assertEquals(actual, expected);

        expected = new Coordinate(-3, 0, Flags.VECTOR);
        actual = odd.getOverbounds(r);
        assertEquals(actual, expected);

        expected = new Coordinate(-1, 1, Flags.VECTOR);
        actual = odd.getOverbounds(s);
        assertEquals(actual, expected);
    }

    public void testDimensions() {
        int[] actual, expected;

        // Odd
        expected = new int[]{3, 5};
        actual = odd.getDimensions();
        assertArraysEqual(actual, expected, false);

        // Even
        expected = new int[]{2, 4};
        actual = even.getDimensions();
        assertArraysEqual(actual, expected, false);
    }

    public void testCloneAtScale() {
        Lattice clonedLattice = evenLattice.clone();
        Shape cloned = even.cloneAtScale(clonedLattice, 2.0);

        assertEquals(even.getClass(), cloned.getClass());
        assertEquals(8, even.getCanonicalSites().length);
        assertEquals(32, cloned.getCanonicalSites().length);
    }

	/*public void testGetLimits() {
        Coordinate[] actual, expected;

		// Even
		expected = new Coordinate[] {
			new Coordinate(0, -1, Flags.VECTOR),
			new Coordinate(1, 2, Flags.VECTOR)
		};
		
		actual = even.getLimits();
		assertArraysEqual(actual, expected, false);
		
		// Odd
		expected = new Coordinate[] {
				new Coordinate(-1, -2, Flags.VECTOR),
				new Coordinate(1, 2, Flags.VECTOR)
			};
		
		actual = odd.getLimits();
		assertArraysEqual(actual, expected, false);
	}*/
}
