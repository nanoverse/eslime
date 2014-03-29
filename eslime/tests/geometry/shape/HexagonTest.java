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

import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.TriangularLattice;
import structural.identifiers.Coordinate;
import structural.identifiers.Flags;
import test.EslimeTestCase;

public class HexagonTest extends EslimeTestCase {

    private Hexagon hex;
    private Lattice lattice;

    @Override
    public void setUp() {
        lattice = new TriangularLattice();

        hex = new Hexagon(lattice, 2);
    }

    public void testGetCenter() {
        Coordinate actual, expected;
        Lattice lattice = new TriangularLattice();

        for (int r = 0; r < 10; r++) {
            hex = new Hexagon(lattice, r);
            expected = new Coordinate(r, r, 0);
            actual = hex.getCenter();
            assertEquals(expected, actual);
        }
    }

    public void testGetBoundaries() {
        Coordinate[] actual, expected;

        expected = new Coordinate[]{
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(1, 3, 0),
                new Coordinate(2, 4, 0),
                new Coordinate(3, 4, 0),
                new Coordinate(4, 4, 0),
                new Coordinate(4, 3, 0),
                new Coordinate(4, 2, 0),
                new Coordinate(3, 1, 0),
                new Coordinate(2, 0, 0),
                new Coordinate(1, 0, 0)
        };

        actual = hex.getBoundaries();
        assertArraysEqual(actual, expected, true);
    }

    public void testCanonicalSites() {

        Coordinate[] actual, expected;

        expected = new Coordinate[]{
                new Coordinate(2, 2, 0),
                new Coordinate(1, 1, 0),
                new Coordinate(1, 2, 0),
                new Coordinate(2, 3, 0),
                new Coordinate(3, 3, 0),
                new Coordinate(3, 2, 0),
                new Coordinate(2, 1, 0),
                new Coordinate(0, 0, 0),
                new Coordinate(0, 1, 0),
                new Coordinate(0, 2, 0),
                new Coordinate(1, 3, 0),
                new Coordinate(2, 4, 0),
                new Coordinate(3, 4, 0),
                new Coordinate(4, 4, 0),
                new Coordinate(4, 3, 0),
                new Coordinate(4, 2, 0),
                new Coordinate(3, 1, 0),
                new Coordinate(2, 0, 0),
                new Coordinate(1, 0, 0)
        };
        actual = hex.getCanonicalSites();
        assertArraysEqual(actual, expected, true);
    }


    public void testOverboundsInside() {


        // In-bounds coordinates -- origin is (2, 2)
        Coordinate a, b, c, d;
        a = new Coordinate(2, 2, 0);
        b = new Coordinate(3, 3, 0);
        c = new Coordinate(2, 0, 0);
        d = new Coordinate(4, 2, 0);

        Coordinate actual, expected;

        expected = new Coordinate(0, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(a);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(b);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(c);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(d);
        assertEquals(expected, actual);
    }


    public void testOverboundsCorners() {
        Coordinate p, q, r, s, t, u;

        p = new Coordinate(5, 2, 0);   // +3u
        q = new Coordinate(5, 5, 0);   // +3v
        r = new Coordinate(2, 5, 0);   // +3w
        s = new Coordinate(-1, 2, 0);  // -3u
        t = new Coordinate(-1, -1, 0); // -3v
        u = new Coordinate(2, -1, 0); // -3w


        Coordinate actual, expected;

        expected = new Coordinate(1, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(p);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 1, 0, Flags.VECTOR);
        actual = hex.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, 1, Flags.VECTOR);
        actual = hex.getOverbounds(r);
        assertEquals(expected, actual);

        expected = new Coordinate(-1, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(s);
        assertEquals(expected, actual);

        expected = new Coordinate(0, -1, 0, Flags.VECTOR);
        actual = hex.getOverbounds(t);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, -1, Flags.VECTOR);
        actual = hex.getOverbounds(u);
        assertEquals(expected, actual);
    }

    public void testOverboundsOffCorners() {
        Coordinate p, q, r, s, t;
        p = new Coordinate(-1, 0, 0); // Expect -u
        q = new Coordinate(-1, 1, 0); // Expect -v
        r = new Coordinate(1, 4, 0);  // Expect -u
        s = new Coordinate(5, 4, 0);  // Expect +u
        t = new Coordinate(5, 3, 0);  // Expect +v

        Coordinate actual, expected;

        expected = new Coordinate(-1, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(p);
        assertEquals(expected, actual);

        expected = new Coordinate(0, -1, 0, Flags.VECTOR);
        actual = hex.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(-1, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(r);
        assertEquals(expected, actual);

        expected = new Coordinate(1, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(s);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 1, 0, Flags.VECTOR);
        actual = hex.getOverbounds(t);
        assertEquals(expected, actual);
    }

    public void testOverboundsAmbiguous() {
        Coordinate p, q, r;
        p = new Coordinate(-1, 3, 0); // Expect -u +w (because we prefer minimum in each direction).
        q = new Coordinate(0, 4, 0);  // Expect -2u (because we prefer u over w).
        r = new Coordinate(4, 6, 0);  // Expect +2w (because we never break a tie in favor of v).

        Coordinate actual, expected;


        expected = new Coordinate(-1, 0, 1, Flags.VECTOR);
        actual = hex.getOverbounds(p);
        assertEquals(expected, actual);

        expected = new Coordinate(-2, 0, 0, Flags.VECTOR);
        actual = hex.getOverbounds(q);
        assertEquals(expected, actual);

        expected = new Coordinate(0, 0, 2, Flags.VECTOR);
        actual = hex.getOverbounds(r);
        assertEquals(expected, actual);
    }

    public void testDimensions() {
        int[] actual, expected;
        expected = new int[]{5, 5, 5};
        actual = hex.getDimensions();
        assertArraysEqual(actual, expected, false);
    }

	/*public void testGetLimits() {
        Coordinate[] actual, expected;

		// Even
		expected = new Coordinate[] {
			new Coordinate(-2, -2, -2, Flags.VECTOR),
			new Coordinate(2, 2, 2, Flags.VECTOR)
		};
		
		actual = hex.getLimits();
		assertArraysEqual(actual, expected, false);
	}*/

    /**
     * Make sure that getNeighbors() returns the correct
     * coordinates.
     */
    public void testNeighbors() {

        // Make a bigger geometry than the one from setUp
        Lattice lattice = new TriangularLattice();
        hex = new Hexagon(lattice, 3);
        Boundary boundary = new Arena(hex, lattice);
        Geometry geometry = new Geometry(lattice, hex, boundary);

        Coordinate query;

        // Check center
        query = new Coordinate(2, 2, 0);
        assertNeighborCount(6, query, geometry);

        // Check corner
        query = new Coordinate(0, 0, 0);
        assertNeighborCount(3, query, geometry);

        // Check side
        query = new Coordinate(1, 0, 0);
        assertNeighborCount(4, query, geometry);
    }

    public void testCloneAtScale() {
        Lattice clonedLattice = lattice.clone();
        Shape cloned = hex.cloneAtScale(clonedLattice, 2.0);

        assertEquals(hex.getClass(), cloned.getClass());
        assertEquals(19, hex.getCanonicalSites().length);
        assertEquals(61, cloned.getCanonicalSites().length);
    }

    private void assertNeighborCount(int expected, Coordinate query, Geometry geometry) {
        Coordinate[] neighbors = geometry.getNeighbors(query, Geometry.EXCLUDE_BOUNDARIES);
        int actual = neighbors.length;
        assertEquals(expected, actual);
    }
}
