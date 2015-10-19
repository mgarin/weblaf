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