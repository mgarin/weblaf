package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JDesktopPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IDesktopPanePainter<C extends JDesktopPane, U extends WebDesktopPaneUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}