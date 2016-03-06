package com.alee.laf.menu;

import java.awt.*;

/**
 * Special interface that allows painters to provide custom popup menu corner painting support.
 *
 * @author Mikle Garin
 */

public interface MenuCornerSupport
{
    /**
     * Returns selected element bounds.
     * Depending on these bounds popup will decide whether or not corner should be affected by the selection.
     *
     * @return selected element bounds
     */
    public Rectangle getSelectedBounds ();

    /**
     * Asks to fill corner according to the selected element.
     * This should basically take clip and corner side into account and paint selection inside the corner shape.
     *
     * @param g2d        graphics context
     * @param clip       preferred painting clip
     * @param corner     corner shape
     * @param cornerSide corner side
     */
    public void fillCorner ( Graphics2D g2d, Rectangle clip, Shape corner, int cornerSide );
}