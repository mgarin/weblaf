package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenuBar component painters.
 *
 * @author Alexandr Zernov
 */

public interface MenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends SpecificPainter<E, U>
{
}