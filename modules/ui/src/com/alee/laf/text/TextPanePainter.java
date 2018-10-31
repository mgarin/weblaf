package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JTextPane} component.
 * It is used as {@link WTextPaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class TextPanePainter<C extends JTextPane, U extends WTextPaneUI, D extends IDecoration<C, D>>
        extends AbstractTextAreaPainter<C, U, D> implements ITextPanePainter<C, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}