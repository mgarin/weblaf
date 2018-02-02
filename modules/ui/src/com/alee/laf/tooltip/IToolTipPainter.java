package com.alee.laf.tooltip;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JToolTip} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IToolTipPainter<C extends JToolTip, U extends WToolTipUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}