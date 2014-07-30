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

package factory.geometry.set;//import junit.framework.TestCase;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.identifiers.Coordinate;
import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.LinearLattice;
import geometry.set.CompleteSet;
import geometry.set.CoordinateSet;
import geometry.set.CustomSet;
import geometry.set.DiscSet;
import geometry.shape.Line;
import geometry.shape.Shape;
import org.dom4j.Element;
import structural.MockGeneralParameters;
import test.EslimeTestCase;

public class CoordinateSetFactoryTest extends EslimeTestCase {

    private Element root;
    private Geometry g;
    private MockGeneralParameters p;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/geometry/set/CoordinateSetFactoryTest.xml");
        p = makeMockGeneralParameters();
        g = makeGeometry();
    }

    public void testDefault() throws Exception {
        // Element does not actually exist, so e is null
        Element e = root.element("default");
        CoordinateSet expected = new CompleteSet(g);
        CoordinateSet actual = CoordinateSetFactory.instantiate(e, g, p);
        assertEquals(expected, actual);
    }

    public void testAll() throws Exception {
        Element e = root.element("all");
        CoordinateSet expected = new CompleteSet(g);
        CoordinateSet actual = CoordinateSetFactory.instantiate(e, g, p);
        assertEquals(expected, actual);
    }

    public void testDisc() throws Exception {
        Element e = root.element("disc");
        Coordinate offset = g.getZeroVector();
        Argument<Integer> radius = new ConstantInteger(1);
        CoordinateSet expected = new DiscSet(g, radius, offset);
        CoordinateSet actual = CoordinateSetFactory.instantiate(e, g, p);
        assertEquals(expected, actual);
    }

    public void testList() throws Exception {
        Element e = root.element("list");
        CoordinateSet expected = new CustomSet();
        CoordinateSet actual = CoordinateSetFactory.instantiate(e, g, p);
        assertEquals(expected, actual);
    }

    private Geometry makeGeometry() {
        Lattice lattice = new LinearLattice();
        Shape shape = new Line(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        Geometry ret = new Geometry(lattice, shape, boundary);
        return ret;
    }

}