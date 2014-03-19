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

package structural;

import org.dom4j.Element;
import test.EslimeTestCase;

/**
 * Created by dbborens on 2/20/14.
 */
public class XmlUtilTest extends EslimeTestCase {
    Element fixtureRoot;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fixtureRoot = readXmlFile("XmlUtilTest.xml");
    }

    public void testGetBoolean() throws Exception {
        Element testRoot = fixtureRoot.element("get-boolean");
        doBooleanTest(testRoot, "flag-only", true);
        doBooleanTest(testRoot, "no-flag", false);
        doBooleanTest(testRoot, "tag-true", true);
        doBooleanTest(testRoot, "tag-false", false);
    }

    private void doBooleanTest(Element testRoot, String childName, boolean expected) {
        Element child = testRoot.element(childName);
        boolean actual = XmlUtil.getBoolean(child, "test");
        assertEquals(expected, actual);
    }

    public void testGetDouble() throws Exception {
        fail("Not yet implemented");
    }
}