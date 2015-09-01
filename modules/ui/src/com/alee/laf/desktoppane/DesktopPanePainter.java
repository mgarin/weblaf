package com.alee.laf.desktoppane;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JDesktopPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface DesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI> extends Painter<E, U>, SpecificPainter
{
}