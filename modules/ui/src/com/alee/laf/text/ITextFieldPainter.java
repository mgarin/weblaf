package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JTextField} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ITextFieldPainter<C extends JTextField, U extends WTextFieldUI> extends IAbstractTextFieldPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}