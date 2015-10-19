package com.alee.managers.style.skin.web;

import com.alee.laf.text.FormattedTextFieldPainter;
import com.alee.laf.text.WebFormattedTextFieldUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebFormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends WebBasicTextFieldPainter<E, U> implements FormattedTextFieldPainter<E, U>, SwingConstants
{
    @Override
    protected String getInputPrompt ()
    {
        return ui.getInputPrompt ();
    }

    @Override
    protected Component getTrailingComponent ()
    {
        return ui.getTrailingComponent ();
    }

    @Override
    protected Component getLeadingComponent ()
    {
        return ui.getLeadingComponent ();
    }

    @Override
    protected void updateInnerComponents ()
    {
        ui.updateInnerComponents ();
    }
}