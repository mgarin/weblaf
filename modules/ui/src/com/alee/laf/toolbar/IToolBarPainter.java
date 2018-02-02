package com.alee.laf.toolbar;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JToolBar} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IToolBarPainter<C extends JToolBar, U extends WebToolBarUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}