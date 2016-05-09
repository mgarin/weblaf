package com.alee.laf.text;

import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Base interface for text field component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IAbstractTextFieldPainter<E extends JTextComponent, U extends BasicTextUI> extends IAbstractTextEditorPainter<E, U>
{
    /**
     * Returns field leading component.
     *
     * @return field leading component
     */
    public Component getLeadingComponent ();

    /**
     * Returns field trailing component.
     *
     * @return field trailing component
     */
    public Component getTrailingComponent ();
}