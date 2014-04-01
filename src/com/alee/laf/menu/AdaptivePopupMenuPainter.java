/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.laf.menu;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple PopupMenuPainter adapter class.
 * It is used to install simple non-specific painters into WebPopupMenuUI.
 *
 * @author Mikle Garin
 */

public class AdaptivePopupMenuPainter<E extends JPopupMenu> extends AdaptivePainter<E> implements PopupMenuPainter<E>
{
    /**
     * Constructs new AdaptiveLabelPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePopupMenuPainter ( final Painter painter )
    {
        super ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransparent ( final boolean transparent )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMenuSpacing ( final int spacing )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFixLocation ( final boolean fix )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPopupMenuWay ( final PopupMenuWay way )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPopupMenuType ( final PopupMenuType type )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point preparePopupMenu ( final E popupMenu, final Component invoker, final int x, final int y )
    {
        return new Point ( x, y );
    }
}