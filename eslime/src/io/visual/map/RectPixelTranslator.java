/*
 *  Copyright (c) 2014 David Bruce Borenstein and the Trustees of
 *  Princeton University. All rights reserved.
 */

package io.visual.map;

import control.identifiers.Coordinate;
import io.visual.VisualizationProperties;

import java.awt.*;

/**
 * Created by David B Borenstein on 5/8/14.
 */
public class RectPixelTranslator extends PixelTranslator {
    @Override
    protected void calcLimits(VisualizationProperties mapState) {
        int xMin, xMax, yMin, yMax;

        xMin = 2147483647;
        xMax = -2147483648;

        yMin = 2147483647;
        yMax = -2147483648;

        for (Coordinate c : mapState.getCoordinates()) {
            int x = c.x();
            int y = c.y();

            if (x < xMin) {
                xMin = x;
            }

            if (x > xMax) {
                xMax = x;
            }

            if (y < yMin) {
                yMin = y;
            }

            if (y > yMax) {
                yMax = y;
            }
        }

        int dy = (int) edge * (yMax - yMin + 1);
        int dx = (int) edge * (xMax - xMin + 1);
        imageDims = new Coordinate(dx, dy, 0);
    }

    @Override
    protected void calcOrigin() {
        int x = (int) Math.round(edge / 2);
        int y = (int) Math.round(edge / 2);
        origin = new Coordinate(x, y, 0);
    }

    protected Coordinate indexToPixels(Coordinate c) {
        int x = c.x();
        int y = c.y();

        int xPixels = (int) Math.round(x * edge);
        int yPixels = (int) Math.round(y * edge);

        Coordinate center = new Coordinate(xPixels, yPixels, 0);

        int px = origin.x() + center.x();
        int py = imageDims.y() - origin.y() - center.y();

        Coordinate ret = new Coordinate(px, py, 0);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof RectPixelTranslator);
    }

    @Override
    public Coordinate resolve(Coordinate c, int frame, double time) {
        return indexToPixels(c);
    }

    @Override
    public Polygon makePolygon(Coordinate c, int frame, double time) {
        Coordinate centerPx = resolve(c, frame, time);
        Polygon p = new Polygon();
        int d = (int) Math.round(edge / 2);

        p.addPoint(centerPx.x() - d, centerPx.y() - d);
        p.addPoint(centerPx.x() + d, centerPx.y() - d);
        p.addPoint(centerPx.x() + d, centerPx.y() + d);
        p.addPoint(centerPx.x() - d, centerPx.y() + d);

        return p;
    }

    @Override
    public double getDiagonal() {
        return Math.sqrt(2) * edge;
    }
}
