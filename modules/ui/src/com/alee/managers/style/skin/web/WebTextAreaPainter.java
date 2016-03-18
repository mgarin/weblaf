package com.alee.managers.style.skin.web;

import com.alee.laf.text.ITextAreaPainter;
import com.alee.laf.text.WebTextAreaUI;
import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebTextAreaPainter<E extends JTextArea, U extends WebTextAreaUI, D extends IDecoration<E, D>>
        extends AbstractTextAreaPainter<E, U, D> implements ITextAreaPainter<E, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}