package com.alee.extended.button;

import com.alee.laf.button.IAbstractButtonPainter;

/**
 * Base interface for {@link WebSplitButton} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public interface ISplitButtonPainter<C extends WebSplitButton, U extends WSplitButtonUI> extends IAbstractButtonPainter<C, U>
{
    /**
     * Returns whether or not mouse is currently over the split menu button.
     *
     * @return true if mouse is currently over the split menu button, false otherwise
     */
    public boolean isOnMenu ();
}