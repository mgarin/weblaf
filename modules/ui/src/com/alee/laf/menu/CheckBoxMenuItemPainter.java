package com.alee.laf.menu;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenuItem component painters.
 *
 * @author Alexandr Zernov
 */

public interface CheckBoxMenuItemPainter<E extends JMenuItem, U extends WebCheckBoxMenuItemUI>
        extends AbstractMenuItemPainter<E, U>, SpecificPainter
{
}