package com.alee.laf.menu;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenu component painters.
 *
 * @author Alexandr Zernov
 */

public interface MenuPainter<E extends JMenu, U extends WebMenuUI> extends Painter<E, U>, SpecificPainter
{
}