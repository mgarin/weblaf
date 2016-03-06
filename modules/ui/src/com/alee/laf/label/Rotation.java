package com.alee.laf.label;

/**
 * Describes common component rotation options.
 *
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public enum Rotation
{
    /**
     * No rotation.
     */
    none,

    /**
     * 90 degrees clockwise rotation.
     * In case with label - first character at top and last one at bottom.
     */
    clockwise,

    /**
     * 90 degrees counter-clockwise rotation.
     * In case with label - first character at bottom and last one at top.
     */
    counterClockwise,

    /**
     * 180 degrees rotation.
     * In case with label - it will be displayed upside down.
     */
    upsideDown;

    /**
     * Return whether orientation vertical or not.
     *
     * @return whether orientation vertical or not.
     */
    public boolean isVertical ()
    {
        return this == Rotation.clockwise || this == Rotation.counterClockwise;
    }

    /**
     * Returns opposite orientation.
     *
     * @return opposite orientation
     */
    public Rotation rightToLeft ()
    {
        switch ( this )
        {
            case clockwise:
                return counterClockwise;

            case counterClockwise:
                return clockwise;

            default:
                return this;
        }
    }
}