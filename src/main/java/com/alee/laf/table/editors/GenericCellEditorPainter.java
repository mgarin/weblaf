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

package com.alee.laf.table.editors;

import com.alee.extended.painter.DefaultPainter;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * User: mgarin Date: 28.11.12 Time: 14:34
 */

public class GenericCellEditorPainter extends DefaultPainter<GenericCellEditor>
{
    public GenericCellEditorPainter ()
    {
        super ();
        setMargin ( 0, 1, 0, 1 );
    }

    public void paint ( Graphics2D g2d, Rectangle bounds, GenericCellEditor c )
    {
        g2d.setPaint ( Color.WHITE );
        g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );

        if ( c.isInvalidValue () )
        {
            Composite old = LafUtils.setupAlphaComposite ( g2d, 0.25f );
            g2d.setPaint ( Color.RED );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );
            LafUtils.restoreComposite ( g2d, old );
        }
    }
}