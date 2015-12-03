package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JInternalFrame.JDesktopIcon component painters.
 *
 * @author Alexandr Zernov
 */

public interface DesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI> extends SpecificPainter<E, U>
{
}