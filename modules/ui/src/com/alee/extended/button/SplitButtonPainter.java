package com.alee.extended.button;

import com.alee.extended.painter.SpecificPainter;
import com.alee.laf.button.AbstractButtonPainter;

/**
 * @author Mikle Garin
 */

public interface SplitButtonPainter<E extends WebSplitButton, U extends WebSplitButtonUI>
        extends AbstractButtonPainter<E, U>, SpecificPainter
{
    /**
     * Returns whether or not mouse is currently over the split menu button.
     *
     * @return true if mouse is currently over the split menu button, false otherwise
     */
    public boolean isOnSplit ();
}