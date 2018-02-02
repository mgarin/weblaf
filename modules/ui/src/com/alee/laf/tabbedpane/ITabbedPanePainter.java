package com.alee.laf.tabbedpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JTabbedPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITabbedPanePainter<C extends JTabbedPane, U extends WTabbedPaneUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}