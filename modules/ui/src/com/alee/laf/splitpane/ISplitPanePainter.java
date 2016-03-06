package com.alee.laf.splitpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSplitPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface ISplitPanePainter<E extends JSplitPane, U extends WebSplitPaneUI> extends SpecificPainter<E, U>
{
}