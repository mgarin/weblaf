package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for JFormattedTextField component painters.
 *
 * @author Alexandr Zernov
 */

public interface IFormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends IAbstractTextFieldPainter<E, U>
{
}