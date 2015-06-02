package com.alee.managers.style.skin.web;

import com.alee.laf.text.PasswordFieldPainter;
import com.alee.laf.text.WebPasswordFieldUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebPasswordFieldPainter<E extends JPasswordField, U extends WebPasswordFieldUI> extends WebBasicTextFieldPainter<E, U>
        implements PasswordFieldPainter<E, U>, SwingConstants
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