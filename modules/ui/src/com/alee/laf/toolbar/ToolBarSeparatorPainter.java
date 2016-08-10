package com.alee.laf.toolbar;

import com.alee.laf.separator.AbstractSeparatorPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JToolBar.Separator} component.
 * It is used as {@link WebToolBarSeparatorUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class ToolBarSeparatorPainter<E extends JToolBar.Separator, U extends WebToolBarSeparatorUI, D extends IDecoration<E, D>>
        extends AbstractSeparatorPainter<E, U, D> implements IToolBarSeparatorPainter<E, U>
{
    /**
     * Implementation is used completely from {@link AbstractSeparatorPainter}.
     */
}