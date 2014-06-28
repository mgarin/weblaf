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

package com.alee.laf.panel;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple PanelPainter adapter class.
 * It is used to install simple non-specific painters into WebPanelUI.
 *
 * @author Mikle Garin
 */

public class AdaptivePanelPainter<E extends JPanel> extends AdaptivePainter<E> implements PanelPainter<E>
{
    /**
     * Constructs new AdaptivePanelPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePanelPainter ( final Painter painter )
    {
        super ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUndecorated ( final boolean undecorated )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintFocus ( final boolean paint )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTop ( final boolean top )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeft ( final boolean left )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottom ( final boolean bottom )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRight ( final boolean right )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintTopLine ( final boolean top )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintLeftLine ( final boolean left )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintBottomLine ( final boolean bottom )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintRightLine ( final boolean right )
    {
        // Ignore this method in adaptive class
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPaintSideLines ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        // Ignore this method in adaptive class
    }
}