package com.alee.laf.menu;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface PopupMenuSeparatorPainter<E extends JSeparator, U extends WebPopupMenuSeparatorUI> extends Painter<E, U>, SpecificPainter
{
}