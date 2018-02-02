package com.alee.laf.toolbar;

import com.alee.laf.separator.IAbstractSeparatorPainter;

import javax.swing.*;

/**
 * Base interface for {@link JSeparator} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IToolBarSeparatorPainter<C extends JToolBar.Separator, U extends WToolBarSeparatorUI>
        extends IAbstractSeparatorPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}