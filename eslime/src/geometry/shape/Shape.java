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

import geometry.lattice.Lattice;
import structural.identifiers.Coordinate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Shape {

    // The set of sites in this geometry, given the lattice type
    protected Coordinate[] canonicalSites;

    // A reverse lookup of coordinate to index into canonicalSites
    protected Map<Coordinate, Integer> coordMap;

    protected Lattice lattice;

    public Shape(Lattice lattice) {
        verify(lattice);
        this.lattice = lattice;

    }

    /**
     * Initialize general data structures. Should be invoked by a Shape
     * subclass after local variables have been assigned.
     */
    protected void init() {
        canonicalSites = calcSites();
        buildCoordMap();
    }

    protected abstract void verify(Lattice lattice);

	
	/* PUBLIC METHODS */

    public Integer coordToIndex(Coordinate coord) {
        if (!coordMap.containsKey(coord)) {
            throw new IllegalArgumentException("Attempted to index non-canonical coordinate " + coord);
        }

        return coordMap.get(coord);
    }


    // Get a complete list of sites for this geometry.
    public Coordinate[] getCanonicalSites() {
        return canonicalSites.clone();
    }

    public abstract Coordinate getCenter();

    public abstract Coordinate[] getBoundaries();

    /**
     * Returns two coordinates representing the limits of the geometry.
     *
     * The coordinates are given as minimum and maximum displacements,
     * in terms of the basis vectors, from the center coordinate.
     *
     */
    //public abstract Coordinate[] getLimits();

    /**
     * Returns a coordinate vector, in the basis of the geometry, of how far
     * over the boundary a particular point is.
     *
     * @param coord
     * @return
     */
    public abstract Coordinate getOverbounds(Coordinate coord);

    /* PROTECTED METHODS */
    protected abstract Coordinate[] calcSites();

    protected void include(Collection<Coordinate> list, Coordinate coordinate) {
        Coordinate adjusted = lattice.adjust(coordinate);
        list.add(adjusted);
    }

	/* PRIVATE METHODS */

    private void buildCoordMap() {

        coordMap = new HashMap<Coordinate, Integer>();
        for (Integer i = 0; i < canonicalSites.length; i++) {
            coordMap.put(canonicalSites[i], i);
        }
    }

    public abstract int[] getDimensions();

    /**
     * In order for shapes to be equal, they must be of the
     * same class and have the same dimensions. They do not
     * need to be associated with the same lattice geometry
     * or have the same boundary conditions in order to be
     * considered equal.
     *
     * @param obj
     * @return
     */
    @Override
    public abstract boolean equals(Object obj);

    public abstract Shape cloneAtScale(Lattice clonedLattice, double rangeScale);
}