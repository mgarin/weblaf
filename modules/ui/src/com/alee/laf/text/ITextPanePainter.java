package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JTextPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITextPanePainter<C extends JTextPane, U extends WTextPaneUI> extends IAbstractTextAreaPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}