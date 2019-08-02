package com.alee.laf.tooltip;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JToolTip} component.
 * It is used as {@link WToolTipUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public class ToolTipPainter<C extends JToolTip, U extends WToolTipUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IToolTipPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractDecorationPainter}.
     */
}