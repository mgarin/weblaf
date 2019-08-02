package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JFormattedTextField} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IFormattedTextFieldPainter<C extends JFormattedTextField, U extends WFormattedTextFieldUI>
        extends IAbstractTextFieldPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}