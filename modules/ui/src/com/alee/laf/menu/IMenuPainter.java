package com.alee.laf.menu;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenu component painters.
 *
 * @author Alexandr Zernov
 */

public interface IMenuPainter<E extends JMenu, U extends WebMenuUI> extends SpecificPainter<E, U>
{
}