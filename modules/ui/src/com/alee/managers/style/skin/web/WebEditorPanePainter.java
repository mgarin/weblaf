package com.alee.managers.style.skin.web;

import com.alee.laf.text.EditorPanePainter;
import com.alee.laf.text.WebEditorPaneUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebEditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI> extends WebBasicTextAreaPainter<E, U>
        implements EditorPanePainter<E, U>
{
}