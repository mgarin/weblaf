package com.alee.laf.rootpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JRootPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public interface IRootPanePainter<C extends JRootPane, U extends WRootPaneUI> extends SpecificPainter<C, U>
{
    /**
     * Returns whether or not this {@link IRootPanePainter} implementation will provide window decoration.
     *
     * @return {@code true} if this {@link IRootPanePainter} implementation will provide window decoration, {@code false} otherwise
     */
    public boolean isDecorated ();
}