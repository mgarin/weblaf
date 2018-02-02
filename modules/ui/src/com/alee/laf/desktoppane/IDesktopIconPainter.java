package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JInternalFrame.JDesktopIcon component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IDesktopIconPainter<C extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}