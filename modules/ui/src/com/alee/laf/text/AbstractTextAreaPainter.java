package com.alee.laf.text;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

/**
 * Abstract painter for {@link JTextComponent}-based text area implementations.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractTextAreaPainter<C extends JTextComponent, U extends BasicTextUI, D extends IDecoration<C, D>>
        extends AbstractTextEditorPainter<C, U, D> implements IAbstractTextAreaPainter<C, U>, SwingConstants
{
    /**
     * Implementation is used completely from {@link AbstractTextEditorPainter}.
     */
}