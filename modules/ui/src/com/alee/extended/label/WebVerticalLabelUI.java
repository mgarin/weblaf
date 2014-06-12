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

package com.alee.extended.label;

import com.alee.laf.label.WebLabelUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Custom UI for WebVerticalLabel component.
 *
 * @author Mikle Garin
 */

public class WebVerticalLabelUI extends WebLabelUI
{
    /**
     * Style settings.
     */
    protected boolean clockwise = false;

    /**
     * Returns an instance of the WebVerticalLabelUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebVerticalLabelUI
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebVerticalLabelUI ();
    }

    public boolean isClockwise ()
    {
        return clockwise;
    }

    public void setClockwise ( final boolean clockwise )
    {
        this.clockwise = clockwise;
    }

    @Override
    public int getBaseline ( final JComponent c, final int width, final int height )
    {
        super.getBaseline ( c, width, height );
        return -1;
    }

    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( final JComponent c )
    {
        super.getBaselineResizeBehavior ( c );
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        final Graphics2D g2 = ( Graphics2D ) g.create ();
        if ( c.getComponentOrientation ().isLeftToRight () ? clockwise : !clockwise )
        {
            g2.rotate ( Math.PI / 2, c.getSize ().width / 2, c.getSize ().width / 2 );
        }
        else
        {
            g2.rotate ( -Math.PI / 2, c.getSize ().height / 2, c.getSize ().height / 2 );
        }
        super.paint ( g2, c );
    }
}