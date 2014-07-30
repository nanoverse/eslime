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

package io.serialize.text;

import control.GeneralParameters;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import layers.LayerManager;
import layers.cell.CellLayer;
import layers.cell.StateMapViewer;
import processes.StepState;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Writes out the number of each "state" as a function of time.
 *
 * @author dbborens
 */
public class CensusWriter extends Serializer {

    private static final String FILENAME = "census.txt";

    // It is necessary to flush all data at the end of each iteration, rather
    // than after each flush event, because a state may appear for the first
    // time in the middle of the simulation, and we want an accurate column
    // for every observed state in the census table.

//    ArrayList<Integer> frames = new ArrayList<>();

    ArrayList<Integer> frames;
    // The keys to this map are FRAMES. The values are a mapping from STATE
    // number to count. If a state number does not appear, that means the
    // count was zero at that time.
    HashMap<Integer, HashMap<Integer, Integer>> histo;
//    HashSet<Integer> observedStates = new HashSet<>();
    HashSet<Integer> observedStates;

    private BufferedWriter bw;

    public CensusWriter(GeneralParameters p) {
        super(p);
    }

    @Override
    public void init(LayerManager lm) {
        super.init(lm);
        histo = new HashMap<>();
        frames = new ArrayList<>();
        observedStates = new HashSet<>();

        String filename = p.getInstancePath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);
    }

    @Override
    public void flush(StepState stepState) {
        CellLayer layer = stepState.getRecordedCellLayer();
        frames.add(stepState.getFrame());

        // Create a bucket for this frame.
        HashMap<Integer, Integer> observations = new HashMap<>();
        histo.put(stepState.getFrame(), observations);

        // Iterate over all observed states for this frame.
        StateMapViewer smv = layer.getViewer().getStateMapViewer();
        for (Integer state : smv.getStates()) {
            Integer count = smv.getCount(state);
            observations.put(state, count);
            observedStates.add(state);
        }
    }

    public void dispatchHalt(HaltCondition ex) {
        conclude();
        closed = true;
    }

    private void conclude() {
        // Sort the states numerically
        TreeSet<Integer> sortedStates = new TreeSet<>(observedStates);

        // Write out the header
        StringBuilder line = new StringBuilder();
        line.append("frame");

        for (Integer state : sortedStates) {
            line.append("\t");
            line.append(state);
        }

        line.append("\n");

        hAppend(bw, line);

        TreeSet<Integer> sortedFrames = new TreeSet<>(histo.keySet());
        for (Integer frame : sortedFrames) {
            HashMap<Integer, Integer> observations = histo.get(frame);

            line = new StringBuilder();
            line.append(frame);

            for (Integer state : sortedStates) {
                line.append("\t");

                if (observations.containsKey(state)) {
                    line.append(observations.get(state));
                } else {
                    line.append("0");
                }
            }

            line.append("\n");
            hAppend(bw, line);

        }
        hClose(bw);

    }

    public void close() {
        // Doesn't do anything.
    }
}
