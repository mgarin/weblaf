package com.alee.laf.toolbar;

import com.alee.laf.separator.AbstractSeparatorPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JToolBar.Separator} component.
 * It is used as {@link WToolBarSeparatorUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class ToolBarSeparatorPainter<C extends JToolBar.Separator, U extends WToolBarSeparatorUI, D extends IDecoration<C, D>>
        extends AbstractSeparatorPainter<C, U, D> implements IToolBarSeparatorPainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractSeparatorPainter}.
     */
}