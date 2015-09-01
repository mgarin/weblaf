package com.alee.laf.splitpane;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSplitPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface SplitPanePainter<E extends JSplitPane, U extends WebSplitPaneUI> extends Painter<E, U>, SpecificPainter
{
}