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

package structural;

/**
 * Created by dbborens on 3/18/14.
 */
public class MockRangeMap<T> extends RangeMap<T> {

    public MockRangeMap() {
    }

    private boolean reportEquality;
    private T nextTarget;

    /**
     * Specifies whether this object should report itself as equal
     * to whatever it happens to be compared to.
     */
    public void setReportEquality(boolean reportEquality) {
        this.reportEquality = reportEquality;
        timesCloned = 0;
    }

    /**
     * Returns true if and only if reportEquality is set to
     * true.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return reportEquality;
    }

    /**
     * Specifies the next target to return, regardless
     * of what "x" is.
     *
     * @param nextTarget
     */
    public void setNextTarget(T nextTarget) {
        this.nextTarget = nextTarget;
    }

    private int timesCloned;

    @Override
    public RangeMap<T> clone() {
        timesCloned++;

        MockRangeMap<T> ret = new MockRangeMap<>();
        ret.setReportEquality(reportEquality);
        ret.setNextTarget(nextTarget);
        return ret;
    }

    @Override
    public T selectTarget(double x) {
        return nextTarget;
    }

    public int getTimesCloned() {
        return timesCloned;
    }
}
