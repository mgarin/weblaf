package com.alee.laf.text;

import javax.swing.*;

/**
 * Base interface for JTextArea component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITextAreaPainter<E extends JTextArea, U extends WebTextAreaUI> extends IAbstractTextAreaPainter<E, U>
{
}