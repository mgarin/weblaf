package com.alee.laf.text;

import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.JTextComponent;

/**
 * Base interface for {@link JTextComponent} painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IAbstractTextAreaPainter<C extends JTextComponent, U extends BasicTextUI> extends IAbstractTextEditorPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}