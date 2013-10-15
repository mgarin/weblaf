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

import com.alee.extended.painter.AbstractPainter;
import com.alee.utils.LafUtils;

import java.awt.*;

/**
 * Custom painter to provide visual feedback for invalid editor cells.
 *
 * @author Mikle Garin
 * @see AbstractPainter
 * @see com.alee.extended.painter.Painter
 */

public class GenericCellEditorPainter extends AbstractPainter<GenericCellEditor>
{
    /**
     * Constructs new generic cell editor painter.
     */
    public GenericCellEditorPainter ()
    {
        super ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ( GenericCellEditor c )
    {
        return new Insets ( 0, 1, 0, 1 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final GenericCellEditor c )
    {
        g2d.setPaint ( Color.WHITE );
        g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );

        if ( c.isInvalidValue () )
        {
            final Composite old = LafUtils.setupAlphaComposite ( g2d, 0.25f );
            g2d.setPaint ( Color.RED );
            g2d.fillRect ( bounds.x, bounds.y, bounds.width, bounds.height );
            LafUtils.restoreComposite ( g2d, old );
        }
    }
}