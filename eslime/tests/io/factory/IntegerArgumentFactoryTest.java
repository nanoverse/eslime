/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.factory;

import control.arguments.Argument;
import control.arguments.ConstantInteger;
import control.arguments.UniformInteger;
import factory.control.arguments.IntegerArgumentFactory;
import org.dom4j.Element;
import test.EslimeTestCase;

import java.util.Random;

/**
 * Created by David B Borenstein on 4/7/14.
 */
public class IntegerArgumentFactoryTest extends EslimeTestCase {

    private Element root;
    private Random random;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = readXmlFile("factories/IntegerArgumentFactoryTest.xml");
        random = new Random(RANDOM_SEED);
    }

    public void testNullNoDefault() {
        Element element = root.element("null-case");

        boolean thrown = false;

        try {
            IntegerArgumentFactory.instantiate(element, "not-there", random);
        } catch (Exception ex) {
            thrown = true;
        }

        assertTrue(thrown);
    }

    public void testNullWithDefault() {
        Element element = root.element("null-case");

        Argument<Integer> actual = IntegerArgumentFactory.instantiate(element, "not-there", 5, random);
        Argument<Integer> expected = new ConstantInteger(5);

        assertEquals(expected, actual);
    }

    public void testConstantImplicit() {
        Element element = root.element("constant-implicit-case");

        Argument<Integer> actual = IntegerArgumentFactory.instantiate(element, "test", 6, random);
        Argument<Integer> expected = new ConstantInteger(5);

        assertEquals(expected, actual);
    }

    public void testConstantExplicit() {
        Argument<Integer> actual = IntegerArgumentFactory.instantiate(root, "constant-explicit-case", 6, random);
        Argument<Integer> expected = new ConstantInteger(5);

        assertEquals(expected, actual);
    }

    public void testUniform() {
        Argument<Integer> actual = IntegerArgumentFactory.instantiate(root, "uniform-case", 6, random);
        Argument<Integer> expected = new UniformInteger(1, 3, random);

        assertEquals(expected, actual);
    }

    public void testRecursive() {
        Argument<Integer> actual = IntegerArgumentFactory.instantiate(root, "recursive-case", 6, random);
        Argument<Integer> expected = new UniformInteger(-1, 2, random);

        assertEquals(expected, actual);
    }
}
