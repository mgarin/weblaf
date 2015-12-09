package com.alee.laf.rootpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JRootPane component painters.
 *
 * @author Alexandr Zernov
 */

public interface IRootPanePainter<E extends JRootPane, U extends WebRootPaneUI> extends SpecificPainter<E, U>
{
}