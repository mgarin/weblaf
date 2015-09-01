package com.alee.laf.menu;

import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JToolBar component painters.
 *
 * @author Alexandr Zernov
 */

public interface MenuItemPainter<E extends JMenuItem, U extends WebMenuItemUI> extends AbstractMenuItemPainter<E, U>, SpecificPainter
{
}