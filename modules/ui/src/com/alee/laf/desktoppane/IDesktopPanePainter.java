package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JDesktopPane component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IDesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI> extends SpecificPainter<E, U>
{
}