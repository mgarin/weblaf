package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JPasswordField} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IPasswordFieldPainter<E extends JPasswordField, U extends WPasswordFieldUI> extends IAbstractTextFieldPainter<E, U>
{
}