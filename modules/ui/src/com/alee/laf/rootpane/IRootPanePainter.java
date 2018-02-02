package com.alee.laf.rootpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JRootPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IRootPanePainter<C extends JRootPane, U extends WRootPaneUI> extends SpecificPainter<C, U>
{
    /**
     * Returns whether or not this painter will provide appropriate window decoration.
     *
     * @return true if this painter will provide appropriate window decoration, false otherwise
     */
    public boolean isDecorated ();
}