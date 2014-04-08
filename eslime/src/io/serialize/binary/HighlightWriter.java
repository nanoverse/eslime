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

package io.serialize.binary;

import control.GeneralParameters;
import control.halt.HaltCondition;
import control.identifiers.Coordinate;
import geometry.Geometry;
import io.serialize.Serializer;
import layers.LayerManager;
import structural.utilities.FileConventions;
import structural.utilities.PrimitiveSerializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbborens on 3/28/14.
 */
public class HighlightWriter extends Serializer {
    private Geometry geometry;
    private Map<Integer, DataOutputStream> streamMap;

    private Integer[] channels;

    public HighlightWriter(GeneralParameters p) {
        super(p);
        makeFiles();

        // For the moment, only one highlight channel is utilized
        channels = new Integer[]{0};
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        closeDataStreams();
    }

    @Override
    public void close() {
    }

    @Override
    public void step(Coordinate[] highlights, double gillespie, int frame) {
        // This method will need to be updated when multi-channel highlighting
        // is implemented in the step(...) signature
        int channel = 0;

        DataOutputStream stream = streamMap.get(channel);
        List<Coordinate> vector = Arrays.asList(highlights);

        PrimitiveSerializer.writeCoercedCoordinateVector(stream, vector, geometry);
    }

    @Override
    public void init(LayerManager layerManager) {
        super.init(layerManager);
        geometry = layerManager.getCellLayer().getGeometry();

        createDataStreams();
    }

    private void createDataStreams() {
        streamMap = new HashMap<>(channels.length);

        for (Integer channel : channels) {
            String baseFilename = FileConventions.makeHighlightFilename(channel);
            String absoluteName = p.getInstancePath() + baseFilename;
            DataOutputStream stream = FileConventions.makeDataOutputStream(absoluteName);
            streamMap.put(channel, stream);
        }
    }

    private void closeDataStreams() {
        try {
            for (DataOutputStream stream : streamMap.values()) {
                stream.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
