package com.alee.laf.text;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JTextArea component painters.
 *
 * @author Alexandr Zernov
 */

public interface TextAreaPainter<E extends JTextArea, U extends WebTextAreaUI> extends AbstractTextAreaPainter<E, U>, SpecificPainter
{
}