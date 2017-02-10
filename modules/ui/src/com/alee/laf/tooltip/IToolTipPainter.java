package com.alee.laf.tooltip;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JToolTip} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IToolTipPainter<E extends JToolTip, U extends WToolTipUI> extends SpecificPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}