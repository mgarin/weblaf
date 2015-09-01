package com.alee.laf.rootpane;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JRootPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface RootPanePainter<E extends JRootPane, U extends WebRootPaneUI> extends Painter<E, U>, SpecificPainter
{
}