package com.alee.laf.text;

import com.alee.laf.text.AbstractTextAreaPainter;
import com.alee.laf.text.ITextPanePainter;
import com.alee.laf.text.WebTextPaneUI;
import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
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