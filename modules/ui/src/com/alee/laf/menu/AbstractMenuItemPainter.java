package com.alee.laf.menu;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicMenuItemUI;

/**
 * Base interface for JMenuItem component painters.
 *
 * @author Alexandr Zernov
 */

public interface AbstractMenuItemPainter<E extends JMenuItem, U extends BasicMenuItemUI> extends Painter<E, U>, SpecificPainter
{
}