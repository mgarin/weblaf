package com.alee.laf.viewport;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JViewport component painters.
 *
 * @author Alexandr Zernov
 */

public interface ViewportPainter<E extends JViewport, U extends WebViewportUI> extends SpecificPainter<E, U>
{
}