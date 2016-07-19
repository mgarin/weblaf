package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JTextArea} component.
 * It is used as {@link WebTextAreaUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class TextAreaPainter<E extends JTextArea, U extends WebTextAreaUI, D extends IDecoration<E, D>>
        extends AbstractTextAreaPainter<E, U, D> implements ITextAreaPainter<E, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}