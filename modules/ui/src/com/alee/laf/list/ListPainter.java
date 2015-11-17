package com.alee.laf.list;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JList component painters.
 *
 * @author Alexandr Zernov
 */

public interface ListPainter<E extends JList, U extends WebListUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Prepares painter to pain list.
     *
     * @param updateLayoutStateNeeded true if need layout updates
     */
    void prepareToPaint ( boolean updateLayoutStateNeeded );
}