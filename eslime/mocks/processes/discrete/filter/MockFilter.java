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

package processes.discrete.filter;

import control.identifiers.Coordinate;

import java.util.Collection;

/**
 * Created by dbborens on 5/15/14.
 */
public class MockFilter extends Filter{
    private boolean called;

    public MockFilter() {
        called = false;
    }

    public boolean isCalled() {
        return called;
    }

    @Override
    public Collection<Coordinate> apply(Collection<Coordinate> toFilter) {
        called = true;
        return toFilter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }
}