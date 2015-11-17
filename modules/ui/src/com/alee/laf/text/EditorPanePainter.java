package com.alee.laf.text;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JEditorPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface EditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI> extends AbstractTextAreaPainter<E, U>, SpecificPainter
{
}