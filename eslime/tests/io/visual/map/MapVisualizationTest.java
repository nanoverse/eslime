/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.map;

import geometry.Geometry;
import geometry.boundaries.Arena;
import geometry.boundaries.Boundary;
import geometry.lattice.Lattice;
import geometry.lattice.RectangularLattice;
import geometry.lattice.TriangularLattice;
import geometry.shape.Hexagon;
import geometry.shape.Rectangle;
import geometry.shape.Shape;
import io.visual.VisualizationProperties;
import io.visual.color.ColorManager;
import io.visual.color.DefaultColorManager;
import io.visual.glyph.Glyph;
import io.visual.glyph.GlyphTest;
import io.visual.glyph.MockGlyph;
import io.visual.highlight.HighlightManager;
import layers.LightweightSystemState;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by dbborens on 4/1/14.
 */
public class MapVisualizationTest extends GlyphTest {
    @Override
    protected Glyph makeGlyph() {
        return new MockGlyph();
    }

    @Override
    protected String getFileName() {
        return "mapVisualizationTest.png";
    }

    @Override
    protected void populateStateAndHealth(Geometry geom, LightweightSystemState systemState) {
        int n = geom.getCanonicalSites().length;
        double[] health = new double[n];
        int[] state = new int[n];

        for (int i = 0; i < n; i++) {
            health[i] = (i % 2) + 1;
            state[i] = ((i + 1) % 2) + 1;
        }
        systemState.initCellLayer(state, health);
    }

    // Regression test for issues with incorrect bounds for hexagonal
    // geometries. (Really a graphical test for HexPixelTranslator.)
    public void testHexagon() throws Exception {
        Lattice lattice = new TriangularLattice();
        Shape shape = new Hexagon(lattice, 10);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        ColorManager colorManager = new DefaultColorManager();
        VisualizationProperties mapState = new VisualizationProperties(colorManager, 25.0, 1);
        HighlightManager highlightManager = new HighlightManager();
        mapState.setHighlightManager(highlightManager);
        MapVisualization map = new MapVisualization(mapState);
        map.init(geom, null, null);
        systemState = makeSystemState(geom);
        BufferedImage result = map.render(systemState);
        File file = new File(outputPath + "HexagonalMap.png");
        ImageIO.write(result, "png", file);

        assertBinaryFilesEqual("glyphs/HexagonalMap.png", "HexagonalMap.png");
    }

    // As above, but for rectangular geometry.
    public void testRectangle() throws Exception {
        doRectangleTest(1, "RectangularMap.png");
    }

    public void testNoOutline() throws Exception {
        doRectangleTest(0, "RectangularMapNoOutline.png");
    }

    private void doRectangleTest(int outline, String filename) throws Exception {
        Lattice lattice = new RectangularLattice();
        Shape shape = new Rectangle(lattice, 5, 5);
        Boundary boundary = new Arena(shape, lattice);
        Geometry geom = new Geometry(lattice, shape, boundary);
        ColorManager colorManager = new DefaultColorManager();
        VisualizationProperties mapState = new VisualizationProperties(colorManager, 25.0, outline);
        HighlightManager highlightManager = new HighlightManager();
        mapState.setHighlightManager(highlightManager);
        MapVisualization map = new MapVisualization(mapState);
        map.init(geom, null, null);
        systemState = makeSystemState(geom);
        BufferedImage result = map.render(systemState);
        File file = new File(outputPath + filename);
        ImageIO.write(result, "png", file);

        assertBinaryFilesEqual("glyphs/" + filename, filename);

    }
}
