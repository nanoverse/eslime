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

package io.deserialize;

import control.identifiers.Coordinate;
import geometry.MockGeometry;
import test.EslimeTestCase;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by David B Borenstein on 3/25/14.
 */
public class PrimitiveDeserializerTest extends EslimeTestCase {
    public void testReadDoubleVector() throws Exception {
        double[] expected = new double[]{5.1, -3.0, 0.0, -0.7, 1e-7};
        DataInputStream input = makeInput("doubleVector.bin");
        double[] actual = PrimitiveDeserializer.readDoubleVector(input);
        assertArraysEqual(expected, actual, false);
    }


    public void testReadIntegerVector() throws Exception {
        int[] expected = new int[]{5, -3, 0, -7, 2};
        DataInputStream input = makeInput("integerVector.bin");
        int[] actual = PrimitiveDeserializer.readIntegerVector(input);
        assertArraysEqual(expected, actual, false);
    }

    public void testReadBooleanVector() throws Exception {
        DataInputStream input = makeInput("booleanVector.bin");
        boolean[] expected = new boolean[]{true, false, true, true, false};
        boolean[] actual = PrimitiveDeserializer.readBooleanVector(input);
        assertArraysEqual(expected, actual);
    }

    public void testReadCoordinateVector() throws Exception {
        DataInputStream input = makeInput("coordinateVector.bin");
        MockGeometry geom = buildMockGeometry();
        MockCoordinateDeindexer deindex = new MockCoordinateDeindexer();
        deindex.setUnderlying(geom.getCanonicalSites());

        int[] indices = new int[]{2, 1, 0, 3, 2};
        Coordinate[] expected = new Coordinate[5];
        for (int i = 0; i < 5; i++) {
            int index = indices[i];
            expected[i] = geom.getCanonicalSites()[index];
        }
        Coordinate[] actual = PrimitiveDeserializer.readCoordinateVector(input, deindex);
        assertArraysEqual(expected, actual, false);
    }

    private DataInputStream makeInput(String filename) throws Exception {
        String fullName = fixturePath + filename;
        File file = new File(fullName);
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        DataInputStream input = new DataInputStream(bufferedInputStream);

        return input;
    }
}
