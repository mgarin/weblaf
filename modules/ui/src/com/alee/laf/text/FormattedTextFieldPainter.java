package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for JFormattedTextField component painters.
 *
 * @author Alexandr Zernov
 */

public interface FormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends AbstractTextFieldPainter<E, U>
{
}