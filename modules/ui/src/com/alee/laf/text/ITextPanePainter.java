package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for JTextPane component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITextPanePainter<E extends JTextPane, U extends WebTextPaneUI> extends IAbstractTextAreaPainter<E, U>
{
}