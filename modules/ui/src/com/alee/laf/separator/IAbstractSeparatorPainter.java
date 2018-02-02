package com.alee.laf.separator;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;

/**
 * Base interface for {@link JSeparator} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IAbstractSeparatorPainter<C extends JSeparator, U extends SeparatorUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}