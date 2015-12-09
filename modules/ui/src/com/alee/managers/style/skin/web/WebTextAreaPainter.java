package com.alee.managers.style.skin.web;

import com.alee.laf.text.ITextAreaPainter;
import com.alee.laf.text.WebTextAreaUI;
import com.alee.managers.language.LM;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebTextAreaPainter<E extends JTextArea, U extends WebTextAreaUI> extends AbstractTextAreaPainter<E, U>
        implements ITextAreaPainter<E, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}