package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for JFormattedTextField component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IFormattedTextFieldPainter<E extends JFormattedTextField, U extends WebFormattedTextFieldUI>
        extends IAbstractTextFieldPainter<E, U>
{
}