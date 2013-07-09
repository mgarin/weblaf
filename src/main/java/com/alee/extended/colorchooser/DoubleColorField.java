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

package com.alee.extended.colorchooser;

import com.alee.laf.colorchooser.HSBColor;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * User: mgarin Date: 08.07.2010 Time: 14:12:13
 */

public class DoubleColorField extends WebPanel
{
    private Color newColor;
    private Color oldColor;

    private HSBColor newHSBColor;
    private HSBColor oldHSBColor;

    public DoubleColorField ()
    {
        super ();
        addMouseListener ( new MouseAdapter ()
        {
            public void mousePressed ( MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) && e.getY () >= getHeight () / 2 )
                {
                    oldColorPressed ();
                }
            }
        } );
    }

    public void paint ( Graphics g )
    {
        super.paint ( g );

        Graphics2D g2d = ( Graphics2D ) g;
        FontMetrics fm = g2d.getFontMetrics ();

        Map hints = SwingUtils.setupTextAntialias ( g2d, this );

        g2d.setPaint ( Color.GRAY );
        g2d.drawRect ( 0, 0, getWidth () - 1, getHeight () - 1 );
        g2d.setPaint ( Color.WHITE );
        g2d.drawRect ( 1, 1, getWidth () - 3, getHeight () - 3 );

        g2d.setPaint ( newColor );
        g2d.fillRect ( 2, 2, getWidth () - 4, getHeight () / 2 - 2 );

        g2d.setPaint ( newHSBColor.getBrightness () >= 0.7f && newHSBColor.getSaturation () < 0.7f ? Color.BLACK : Color.WHITE );
        g2d.drawString ( "new", getWidth () / 2 - fm.stringWidth ( "new" ) / 2, ( getHeight () - 4 ) / 4 + fm.getAscent () / 2 );

        g2d.setPaint ( oldColor );
        g2d.fillRect ( 2, getHeight () / 2, getWidth () - 4, getHeight () - getHeight () / 2 - 2 );

        g2d.setPaint ( oldHSBColor.getBrightness () >= 0.7f && oldHSBColor.getSaturation () < 0.7f ? Color.BLACK : Color.WHITE );
        g2d.drawString ( "current", getWidth () / 2 - fm.stringWidth ( "current" ) / 2,
                ( getHeight () - 4 ) * 3 / 4 + fm.getAscent () / 2 );

        SwingUtils.restoreTextAntialias ( g2d, hints );
    }

    public Color getNewColor ()
    {
        return newColor;
    }

    public void setNewColor ( Color newColor )
    {
        this.newColor = newColor;
        this.newHSBColor = new HSBColor ( newColor );
        this.repaint ();
    }

    public Color getOldColor ()
    {
        return oldColor;
    }

    public void setOldColor ( Color oldColor )
    {
        this.oldColor = oldColor;
        this.oldHSBColor = new HSBColor ( oldColor );
        this.repaint ();
    }

    protected void oldColorPressed ()
    {
        //
    }
}
