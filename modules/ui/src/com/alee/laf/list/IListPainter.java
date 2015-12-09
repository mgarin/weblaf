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
     * Prepares painter to pain list.
     *
     * @param updateLayoutStateNeeded true if need layout updates
     */
    public void prepareToPaint ( boolean updateLayoutStateNeeded );
}