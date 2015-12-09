package com.alee.managers.style.skin.web;

import com.alee.laf.text.IFormattedTextFieldPainter;
import com.alee.laf.text.WebFormattedTextFieldUI;
import com.alee.managers.language.LM;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebFormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends AbstractTextFieldPainter<E, U> implements IFormattedTextFieldPainter<E, U>, SwingConstants
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }

    @Override
    public Component getTrailingComponent ()
    {
        return ui.getTrailingComponent ();
    }

    @Override
    public Component getLeadingComponent ()
    {
        return ui.getLeadingComponent ();
    }
}