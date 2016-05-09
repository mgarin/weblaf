package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for JEditorPane component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IEditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI> extends IAbstractTextAreaPainter<E, U>
{
}