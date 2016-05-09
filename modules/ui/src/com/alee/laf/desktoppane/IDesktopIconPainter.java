package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JInternalFrame.JDesktopIcon component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IDesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI> extends SpecificPainter<E, U>
{
}