package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JTextField} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITextFieldPainter<E extends JTextField, U extends WTextFieldUI> extends IAbstractTextFieldPainter<E, U>
{
}