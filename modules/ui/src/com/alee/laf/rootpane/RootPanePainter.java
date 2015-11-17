package com.alee.laf.rootpane;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JRootPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface RootPanePainter<E extends JRootPane, U extends WebRootPaneUI> extends Painter<E, U>, SpecificPainter
{
}