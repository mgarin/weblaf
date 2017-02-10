package com.alee.laf.tooltip;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JToolTip} component.
 * It is used as {@link WToolTipUI} default painter.
 *
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class ToolTipPainter<E extends JToolTip, U extends WToolTipUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IToolTipPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractDecorationPainter}.
     */
}