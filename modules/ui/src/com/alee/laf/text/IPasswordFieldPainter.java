package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JPasswordField} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IPasswordFieldPainter<C extends JPasswordField, U extends WPasswordFieldUI> extends IAbstractTextFieldPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}