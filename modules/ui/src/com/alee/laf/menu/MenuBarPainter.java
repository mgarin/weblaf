package com.alee.laf.menu;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenuBar component painters.
 *
 * @author Alexandr Zernov
 */

public interface MenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends Painter<E, U>, SpecificPainter
{
}