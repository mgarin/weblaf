package com.alee.managers.style.skin.web;

import com.alee.laf.text.TextFieldPainter;
import com.alee.laf.text.WebTextFieldUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebTextFieldPainter<E extends JTextField, U extends WebTextFieldUI> extends WebBasicTextFieldPainter<E, U>
        implements TextFieldPainter<E, U>, SwingConstants
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