package com.alee.laf.text;

import com.alee.laf.text.AbstractTextEditorPainter;
import com.alee.laf.text.IAbstractTextAreaPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

/**
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public abstract class AbstractTextAreaPainter<E extends JTextComponent, U extends BasicTextUI, D extends IDecoration<E, D>>
        extends AbstractTextEditorPainter<E, U, D> implements IAbstractTextAreaPainter<E, U>, SwingConstants
{
}