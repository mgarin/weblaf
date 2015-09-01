package com.alee.laf.text;

import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JTextPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface TextPanePainter<E extends JTextPane, U extends WebTextPaneUI> extends AbstractTextAreaPainter<E, U>, SpecificPainter
{
}