package com.alee.managers.style.skin.web;

import com.alee.laf.text.FormattedTextFieldPainter;
import com.alee.laf.text.WebFormattedTextFieldUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebFormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends BasicTextFieldPainter<E, U> implements FormattedTextFieldPainter<E, U>, SwingConstants
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInputPrompt ()
    {
        return ui.getInputPrompt ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component getTrailingComponent ()
    {
        return ui.getTrailingComponent ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Component getLeadingComponent ()
    {
        return ui.getLeadingComponent ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateInnerComponents ()
    {
        ui.updateInnerComponents ();
    }
}