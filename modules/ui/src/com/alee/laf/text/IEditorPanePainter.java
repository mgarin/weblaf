package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JEditorPane} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IEditorPanePainter<C extends JEditorPane, U extends WEditorPaneUI> extends IAbstractTextAreaPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}