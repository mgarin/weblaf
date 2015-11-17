package com.alee.laf.text;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JPasswordField component painters.
 *
 * @author Alexandr Zernov
 */

public interface PasswordFieldPainter<E extends JPasswordField, U extends WebPasswordFieldUI>
        extends AbstractTextFieldPainter<E, U>, SpecificPainter
{
}