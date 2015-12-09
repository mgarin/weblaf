package com.alee.extended.button;

import com.alee.laf.button.IAbstractButtonPainter;

/**
 * @author Mikle Garin
 */

public interface ISplitButtonPainter<E extends WebSplitButton, U extends WebSplitButtonUI> extends IAbstractButtonPainter<E, U>
{
    /**
     * Returns whether or not mouse is currently over the split menu button.
     *
     * @return true if mouse is currently over the split menu button, false otherwise
     */
    public boolean isOnSplit ();
}