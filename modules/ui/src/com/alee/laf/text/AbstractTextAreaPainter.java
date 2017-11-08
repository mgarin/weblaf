package com.alee.laf.text;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

/**
 * Abstract painter for {@link JTextComponent}-based text area implementations.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public abstract class AbstractTextAreaPainter<E extends JTextComponent, U extends BasicTextUI, D extends IDecoration<E, D>>
        extends AbstractTextEditorPainter<E, U, D> implements IAbstractTextAreaPainter<E, U>, SwingConstants
{
    /**
     * Implementation is used completely from {@link AbstractTextEditorPainter}.
     */
}