package com.alee.laf.label;

/**
 * @author Alexandr Zernov
 */

public enum LabelOrientation
{
    /**
     * Normal label.
     */
    normal,

    /**
     * Clockwise-turned label (with first character at top and last one at bottom).
     */
    clockwise,

    /**
     * Counter-clockwise-turned label (with first character at bottom and last one at top).
     */
    counterClockwise,

    /**
     * Upside down label.
     */
    upsideDown;

    /**
     * Returns opposite orientation.
     *
     * @return opposite orientation
     */
    public LabelOrientation opposite ()
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