package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for {@link JTextPane} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITextPanePainter<E extends JTextPane, U extends WTextPaneUI> extends IAbstractTextAreaPainter<E, U>
{
}