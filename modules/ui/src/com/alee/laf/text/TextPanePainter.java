package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JTextPane} component.
 * It is used as {@link WebTextPaneUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class TextPanePainter<E extends JTextPane, U extends WebTextPaneUI, D extends IDecoration<E, D>>
        extends AbstractTextAreaPainter<E, U, D> implements ITextPanePainter<E, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}