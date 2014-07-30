/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.serialize.text;

import control.GeneralParameters;
import control.halt.FixationEvent;
import control.halt.HaltCondition;
import io.serialize.Serializer;
import processes.StepState;

import java.io.BufferedWriter;

public class HaltTimeWriter extends Serializer {

    private static final String FILENAME = "tth.txt";
    private BufferedWriter bw;

    public HaltTimeWriter(GeneralParameters p) {
        super(p);

        // We use the non-instance path because this metric aggregates over
        // all instances.
        String filename = p.getPath() + '/' + FILENAME;
        mkDir(p.getInstancePath(), true);
        bw = makeBufferedWriter(filename);

        hAppend(bw, new StringBuilder("gillespie\thalt_info\n"));
    }

    @Override
    public void flush(StepState stepState) {

    }

    @Override
    public void close() {
        closed = true;
        hClose(bw);
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        StringBuilder line = new StringBuilder();
        line.append(ex.getGillespie());
        line.append("\t");
        line.append(ex.toString());
        line.append("\n");
        hAppend(bw, line);
        closed = true;
    }
    private StringBuilder fixationLine(FixationEvent fix) {
        StringBuilder sb = new StringBuilder();
        sb.append(fix.getGillespie());
        sb.append("\t");
        sb.append(fix.getFixationState());
        sb.append("\n");
        return sb;
    }

}
