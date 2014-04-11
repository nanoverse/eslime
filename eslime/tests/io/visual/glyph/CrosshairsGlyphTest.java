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

package io.visual.glyph;

import java.awt.*;

/**
 * Integration test of the dot glyph (a filled dot at the center of the cell).
 * <p/>
 * Created by dbborens on 4/3/14.
 */
public class CrosshairsGlyphTest extends GlyphTest {

    @Override
    protected Glyph makeGlyph() {
        return new CrosshairsGlyph(Color.decode("4742424"), 0.15, 1.5);
    }

    @Override
    protected String getFileName() {
        return "crosshairsGlyph.png";
    }
}