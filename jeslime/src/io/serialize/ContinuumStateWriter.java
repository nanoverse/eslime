package io.serialize;

import geometry.Geometry;
import io.project.GeometryManager;
import layers.LayerManager;
import layers.solute.SoluteLayer;
import no.uib.cipr.matrix.DenseVector;
import structural.GeneralParameters;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;
import structural.identifiers.Extrema;

import java.io.*;

/**
 * Created by dbborens on 12/11/13.
 *
 * ContinuumStateWriter encodes a binary file containing the
 * state of the model at specified time points.
 *
 */
public class ContinuumStateWriter extends AbstractContinuumWriter {

    private SoluteLayer layer;
    private Extrema extrema;

    private static String prefix = "solute";
    private static String stateSuffix = ".state.txt";
    private static String mdSuffix = ".metadata.txt";

    private DataOutputStream dataStream;

    // Canonical sites (for this instance)
    private Coordinate[] sites;


    public ContinuumStateWriter(GeneralParameters p, LayerManager lm) {
        super(lm, p);
    }

    @Override
    public void init(SoluteLayer l) {
        if (!closed) {
            throw new IllegalStateException("Attempting to initialize active writer!");
        }

        layer = l;

        initStructures();

        makeFiles();

        initFiles();
    }

    private void initFiles() {
        String filename = makeFileName();
        String filepath = p.getInstancePath() + '/' + filename;

        try {

            File stateFile = new File(filepath);

            FileOutputStream fileOutputStream = new FileOutputStream(stateFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            dataStream = new DataOutputStream(bufferedOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String makeFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(layer.getId());
        sb.append(stateSuffix);
        return sb.toString();
    }

    public void initStructures() {
        Geometry geometry = layer.getGeometry();
        sites = geometry.getCanonicalSites();

        // Initialize extrema
        extrema = new Extrema();
    }

    @Override
    public void step(double gillespie, int frame) {
        try {
            // Write opening parity sequence
            writeStartParitySequence();

            // Write entry header
            dataStream.writeDouble(gillespie);
            dataStream.writeInt(frame);

            // Process state vector
            processData(frame);

            // Write closing parity sequence
            writeEndParitySequence();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Write the state vector to the data stream and update extrema.
     * @param frame
     * @throws IOException
     */
    private void processData(int frame) throws IOException {
        DenseVector data = layer.getState().getSolution();

        // Encode length (as a reality check)
        int length = data.size();
        dataStream.writeInt(length);
        for (int i = 0; i < data.size(); i++) {
            double datum = data.get(i);
            dataStream.writeDouble(data.get(i));
            extrema.consider(datum, sites[i], frame);
        }
    }

    /**
     *     Encode parity sequence for entry start
     */
    private void writeStartParitySequence() throws IOException {
        for (int i = 0; i < 2; i++) {
            dataStream.writeBoolean(true);
        }
    }

    /**
     *     Encode parity sequence for entry end
     */
    private void writeEndParitySequence() throws IOException {
        for (int i = 0; i < 2; i++) {
            dataStream.writeBoolean(false);
        }
    }

    @Override
    public void dispatchHalt(HaltCondition ex) {
        conclude();
        closed = true;
    }

    private void conclude() {
        // Close the state data file.
        try {
            dataStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeMetadata();

    }

    private void writeMetadata() {
        // Write the extrema file.
        try {
            File metadata = new File(getMetadataFilename());
            String filepath = p.getInstancePath() + '/' + metadata;
            FileWriter mfw = new FileWriter(filepath);
            BufferedWriter mbw = new BufferedWriter(mfw);

            mbw.write("extrema>");
            mbw.write(extrema.toString());
            mbw.write('\n');

            mbw.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMetadataFilename() {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(layer.getId());
        sb.append(mdSuffix);
        return sb.toString();
    }

    @Override
    public void close() {
        // Doesn't do anything
    }

}
