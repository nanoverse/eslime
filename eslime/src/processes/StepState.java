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

package processes;

import control.identifiers.Coordinate;
import layers.cell.CellLayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * State object for a cycle in a simulation. Each
 * process can modify this state object as necessary.
 * <p/>
 * When the processes are done, information from this
 * state object is passed to appropriate downstream
 * elements.
 *
 * @author dbborens
 */
public class StepState {

    private CellLayer recordedCellLayer;
    private boolean recorded;

    private HashMap<Integer, Set<Coordinate>> highlights;
    private double dt;
    private double startTime;
    private int frame;

    public StepState(double startTime, int frame) {
        highlights = new HashMap<>();
        dt = 0;
        this.startTime = startTime;
        this.frame = frame;
        this.recorded = false;
    }

    public void highlight(Coordinate c, Integer channel) {
        if (!highlights.containsKey(channel)) {
            highlights.put(channel, new HashSet<Coordinate>());
        }
        Set<Coordinate> set = highlights.get(channel);
        set.add(c);
    }

    public void advanceClock(double time) {
        dt += time;
    }

    public double getDt() {
        return dt;
    }

    public Coordinate[] getHighlights(Integer channel) {
        if (!highlights.containsKey(channel)) {
            return new Coordinate[0];
        }

        Set<Coordinate> set = highlights.get(channel);

        return set.toArray(new Coordinate[set.size()]);
    }

    public boolean isRecorded() {
        return recorded;
    }

    public void record(CellLayer cellLayer) {
        recordedCellLayer = cellLayer.clone();
        recorded = true;

    }

    public double getTime() {
        return startTime + dt;
    }

    public int getFrame() {
        return frame;
    }

    public CellLayer getRecordedCellLayer() {
        return recordedCellLayer;
    }
}
