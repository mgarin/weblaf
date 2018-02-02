package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JMenu} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IMenuPainter<C extends JMenu, U extends WebMenuUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}