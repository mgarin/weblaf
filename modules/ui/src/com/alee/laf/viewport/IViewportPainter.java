package com.alee.laf.viewport;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JViewport component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IViewportPainter<E extends JViewport, U extends WebViewportUI> extends SpecificPainter<E, U>
{
}