package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JTextArea} component.
 * It is used as {@link WTextAreaUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class TextAreaPainter<C extends JTextArea, U extends WTextAreaUI, D extends IDecoration<C, D>>
        extends AbstractTextAreaPainter<C, U, D> implements ITextAreaPainter<C, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}