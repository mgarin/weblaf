package com.alee.laf.menu;

import com.alee.laf.separator.AbstractSeparatorPainter;

import javax.swing.*;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface PopupMenuSeparatorPainter<E extends JPopupMenu.Separator, U extends WebPopupMenuSeparatorUI>
        extends AbstractSeparatorPainter<E, U>
{
}