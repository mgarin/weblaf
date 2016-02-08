package com.alee.laf.list;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JList component painters.
 *
 * @author Alexandr Zernov
 */

public interface IListPainter<E extends JList, U extends WebListUI> extends SpecificPainter<E, U>
{
    /**
     * Returns whether or not hover item decoration is supported by this list painter.
     *
     * @return true if hover item decoration is supported by this list painter, false otherwise
     */
    public boolean isHoverDecorationSupported ();

    /**
     * Prepares painter to pain list.
     *
     * @param updateLayoutStateNeeded true if need layout updates
     */
    public void prepareToPaint ( boolean updateLayoutStateNeeded );
}