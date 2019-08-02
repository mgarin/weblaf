package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JTextArea} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ITextAreaPainter<C extends JTextArea, U extends WTextAreaUI> extends IAbstractTextAreaPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}